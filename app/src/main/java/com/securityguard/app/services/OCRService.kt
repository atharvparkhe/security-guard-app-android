package com.securityguard.app.services

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class OCRInvoiceResult(
    val rawText: String,
    val invoiceNumber: String? = null,
    val vendorName: String? = null,
    val amount: String? = null,
    val stage: OCRStage,
)

enum class OCRStage {
    ML_KIT,
    MOCK_LLAMA,
}

@Singleton
class OCRService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    /**
     * Stage 1: ML Kit Text Recognition on a scanned document bitmap.
     */
    suspend fun recognizeWithMlKit(bitmap: Bitmap): OCRInvoiceResult {
        val image = InputImage.fromBitmap(bitmap, 0)
        val raw = recognizeText(image)
        return OCRInvoiceResult(
            rawText = raw,
            invoiceNumber = extractInvoiceNumber(raw),
            vendorName = extractVendor(raw),
            amount = extractAmount(raw),
            stage = OCRStage.ML_KIT,
        )
    }

    /**
     * Stage 2: llama.cpp JNI for structured extraction.
     *
     * TODO: Integrate llama.cpp JNI and load GGUF from resolved path (do NOT bundle in APK):
     *   ../ios/security-guard-app/localModels/Phi-3-mini-4k-instruct-q4.gguf
     * Resolve relative to app files dir or external files at runtime.
     */
    suspend fun refineWithLocalLlm(stage1: OCRInvoiceResult): OCRInvoiceResult {
        val modelPath = resolvePhi3ModelPath()
        if (modelPath == null || !File(modelPath).exists()) {
            return mockOcrResult(stage1.rawText)
        }
        // TODO: llama.cpp JNI — run Phi-3-mini-4k-instruct-q4 on stage1.rawText
        return mockOcrResult(stage1.rawText)
    }

    fun resolvePhi3ModelPath(): String? {
        val relative = File(
            context.filesDir.parentFile?.parentFile?.parentFile?.parentFile,
            "ios/security-guard-app/localModels/Phi-3-mini-4k-instruct-q4.gguf",
        )
        if (relative.exists()) return relative.absolutePath

        val fromCwd = File("../ios/security-guard-app/localModels/Phi-3-mini-4k-instruct-q4.gguf")
        if (fromCwd.exists()) return fromCwd.absolutePath

        return null
    }

    private fun mockOcrResult(rawText: String): OCRInvoiceResult = OCRInvoiceResult(
        rawText = rawText,
        invoiceNumber = extractInvoiceNumber(rawText) ?: "INV-MOCK-001",
        vendorName = extractVendor(rawText) ?: "Mock Vendor Ltd",
        amount = extractAmount(rawText) ?: "12500.00",
        stage = OCRStage.MOCK_LLAMA,
    )

    private suspend fun recognizeText(image: InputImage): String = suspendCancellableCoroutine { cont ->
        textRecognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text.trim()
                if (text.isEmpty()) {
                    cont.resumeWithException(IllegalStateException("No text detected on document."))
                } else {
                    cont.resume(text)
                }
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    private fun extractInvoiceNumber(text: String): String? {
        val regex = Regex("""(?i)invoice\s*#?\s*[:.]?\s*([A-Z0-9\-/]+)""")
        return regex.find(text)?.groupValues?.getOrNull(1)
    }

    private fun extractVendor(text: String): String? {
        val lines = text.lines().map { it.trim() }.filter { it.isNotEmpty() }
        return lines.firstOrNull { it.length in 3..40 && !it.contains("invoice", ignoreCase = true) }
    }

    private fun extractAmount(text: String): String? {
        val regex = Regex("""(?i)(?:total|amount)\s*[:.]?\s*(?:Rs\.?|INR)?\s*([\d,]+\.?\d*)""")
        return regex.find(text)?.groupValues?.getOrNull(1)
    }

    fun release() {
        textRecognizer.close()
    }
}
