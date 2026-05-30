package com.securityguard.app.models

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

enum class UserRole(val apiValue: String) {
    SECURITY_GUARD("security_guard"),
    STORES("stores"),
}

enum class InwardEntryStatus(val apiValue: String, val displayName: String) {
    DRAFT("draft", "Draft"),
    INVOICE_UPLOADED("invoice_uploaded", "Invoice Uploaded"),
    PENDING_VERIFICATION("pending_verification", "Pending Verification"),
    ACKNOWLEDGED("acknowledged", "Acknowledged"),
    GRN_GENERATED("grn_generated", "GRN Ready"),
    REJECTED("rejected", "Rejected"),
    COMPLETED("completed", "Completed"),
}

data class MaterialItem(
    val description: String,
    val quantity: String,
    val unit: String,
)

data class StatusLogEntry(
    val fromStatus: InwardEntryStatus?,
    val toStatus: InwardEntryStatus,
    val changedByName: String,
    val notes: String,
    val changedAt: Instant = Instant.now(),
)

data class InwardEntry(
    val id: String = UUID.randomUUID().toString(),
    val status: InwardEntryStatus,
    val truckRegistration: String,
    val driverName: String,
    val driverMobile: String,
    val driverLicence: String,
    val poNumber: String? = null,
    val vendorName: String = "",
    val challanNumber: String = "",
    val invoiceNumber: String = "",
    val invoiceDate: Instant? = null,
    val invoiceAmount: BigDecimal? = null,
    val invoiceReceived: Boolean = false,
    val materials: List<MaterialItem> = emptyList(),
    val guardRemarks: String = "",
    val grnNumber: String = "",
    val grnDate: Instant? = null,
    val rejectionCategory: String = "",
    val rejectionReason: String = "",
    val inTime: Instant? = null,
    val outTime: Instant? = null,
    val statusLogs: List<StatusLogEntry> = emptyList(),
    val invoiceFileUrl: String? = null,
) {
    val isExitLocked: Boolean
        get() = status in listOf(
            InwardEntryStatus.DRAFT,
            InwardEntryStatus.INVOICE_UPLOADED,
            InwardEntryStatus.PENDING_VERIFICATION,
        )

    companion object {
        fun mock(
            status: InwardEntryStatus = InwardEntryStatus.PENDING_VERIFICATION,
            truck: String = "MH12AB1234",
        ) = InwardEntry(
            status = status,
            truckRegistration = truck,
            driverName = "Rajesh Kumar",
            driverMobile = "9876543210",
            driverLicence = "MH0123456789",
            poNumber = "PO-2026-001",
            vendorName = "Steel Supplies Ltd",
            invoiceNumber = "INV-8842",
            inTime = if (status != InwardEntryStatus.DRAFT) Instant.now().minusSeconds(3600) else null,
        )
    }
}

enum class OutwardType(val displayName: String) {
    RETURNABLE("Returnable"),
    NON_RETURNABLE("Non-returnable"),
}

data class OutwardEntry(
    val id: String = UUID.randomUUID().toString(),
    val type: OutwardType,
    val truckRegistration: String,
    val driverName: String,
    val purpose: String,
    val status: String = "created",
    val inTime: Instant? = null,
    val outTime: Instant? = null,
) {
    companion object {
        fun mock() = OutwardEntry(
            type = OutwardType.RETURNABLE,
            truckRegistration = "MH14XY9876",
            driverName = "Suresh Patil",
            purpose = "Tooling return to vendor",
            inTime = Instant.now().minusSeconds(1800),
        )
    }
}

data class VisitorEntry(
    val id: String = UUID.randomUUID().toString(),
    val visitorName: String,
    val company: String,
    val purpose: String,
    val hostName: String,
    val ndaSigned: Boolean,
    val inTime: Instant? = null,
    val outTime: Instant? = null,
) {
    companion object {
        fun mock(inside: Boolean = true) = VisitorEntry(
            visitorName = "Anita Desai",
            company = "Tech Audit Co",
            purpose = "IT security review",
            hostName = "Plant Manager",
            ndaSigned = true,
            inTime = if (inside) Instant.now().minusSeconds(2400) else null,
        )
    }
}

data class InwardListStats(
    val total: Int = 0,
    val inside: Int = 0,
    val pending: Int = 0,
) {
    companion object {
        fun mock() = InwardListStats(total = 12, inside = 3, pending = 4)
    }
}

data class InwardListResult(
    val entries: List<InwardEntry>,
    val stats: InwardListStats,
) {
    companion object {
        fun mock() = InwardListResult(
            entries = listOf(
                InwardEntry.mock(InwardEntryStatus.PENDING_VERIFICATION),
                InwardEntry.mock(InwardEntryStatus.GRN_GENERATED, "KA05CD4321"),
                InwardEntry.mock(InwardEntryStatus.DRAFT, "GJ01EF5678"),
            ),
            stats = InwardListStats.mock(),
        )
    }
}
