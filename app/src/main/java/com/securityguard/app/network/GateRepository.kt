package com.securityguard.app.network

import com.securityguard.app.models.InwardEntry
import com.securityguard.app.models.InwardListResult
import com.securityguard.app.models.OutwardEntry
import com.securityguard.app.models.VisitorEntry
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GateRepository @Inject constructor(
    private val gateApi: GateApi,
) {
    private val cache = mutableMapOf<String, InwardEntry>()

    // TODO: Replace with actual API call — GET inward/
    suspend fun fetchInwardEntries(fromDate: String, toDate: String, status: String? = null): InwardListResult {
        delay(MOCK_DELAY_MS)
        val result = InwardListResult.mock()
        result.entries.forEach { cache[it.id] = it }
        return result
    }

    // TODO: Replace with actual API call — GET inward/{id}/
    suspend fun fetchInwardEntry(id: String): InwardEntry {
        delay(MOCK_DELAY_MS)
        return cache[id] ?: InwardEntry.mock().copy(id = id)
    }

    // TODO: Replace with actual API call — POST inward/{id}/allow-in/
    suspend fun allowInInward(id: String, inTime: Instant?): InwardEntry {
        delay(MOCK_DELAY_MS)
        val entry = cache[id] ?: InwardEntry.mock()
        val updated = entry.copy(inTime = inTime ?: Instant.now())
        cache[id] = updated
        return updated
    }

    // TODO: Replace with actual API call — POST inward/{id}/mark-exit/
    suspend fun markExitInward(id: String, outTime: Instant?): InwardEntry {
        delay(MOCK_DELAY_MS)
        val entry = cache[id] ?: InwardEntry.mock()
        val updated = entry.copy(outTime = outTime ?: Instant.now())
        cache[id] = updated
        return updated
    }

    // TODO: Replace with actual API call — GET outward/
    suspend fun fetchOutwardEntries(fromDate: String, toDate: String): List<OutwardEntry> {
        delay(MOCK_DELAY_MS)
        return listOf(OutwardEntry.mock(), OutwardEntry.mock().copy(id = "out-2", truckRegistration = "DL8CA9999"))
    }

    // TODO: Replace with actual API call — GET visitors/
    suspend fun fetchVisitorEntries(fromDate: String, toDate: String): List<VisitorEntry> {
        delay(MOCK_DELAY_MS)
        return listOf(VisitorEntry.mock(), VisitorEntry.mock(inside = false).copy(id = "vis-2", visitorName = "Ravi Mehta"))
    }

    // TODO: Replace with actual API call — POST outward/{id}/allow-in/
    suspend fun allowInOutward(id: String, inTime: Instant?): OutwardEntry {
        delay(MOCK_DELAY_MS)
        return OutwardEntry.mock().copy(id = id, inTime = inTime ?: Instant.now())
    }

    // TODO: Replace with actual API call — POST visitors/{id}/allow-in/
    suspend fun allowInVisitor(id: String, inTime: Instant?): VisitorEntry {
        delay(MOCK_DELAY_MS)
        return VisitorEntry.mock().copy(id = id, inTime = inTime ?: Instant.now())
    }

    fun todayQueryDate(): String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    companion object {
        private const val MOCK_DELAY_MS = 350L
    }
}
