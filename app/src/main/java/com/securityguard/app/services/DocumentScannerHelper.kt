package com.securityguard.app.services

import android.app.Activity
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

object DocumentScannerHelper {

    fun createScanner(): GmsDocumentScanner {
        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(1)
            .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()
        return GmsDocumentScanning.getClient(options)
    }

    suspend fun getScanIntentSenderRequest(activity: Activity): IntentSenderRequest? {
        val scanner = createScanner()
        return try {
            val sender: IntentSender = scanner.getStartScanIntent(activity).awaitSender()
            IntentSenderRequest.Builder(sender).build()
        } catch (_: Exception) {
            null
        }
    }

    fun firstPageUri(result: GmsDocumentScanningResult): android.net.Uri? =
        result.pages?.firstOrNull()?.imageUri

    private suspend fun com.google.android.gms.tasks.Task<IntentSender>.awaitSender(): IntentSender {
        return kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            addOnSuccessListener { cont.resume(it, null) }
            addOnFailureListener { cont.cancel(it) }
        }
    }
}
