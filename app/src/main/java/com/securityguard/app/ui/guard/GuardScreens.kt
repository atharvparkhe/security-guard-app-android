package com.securityguard.app.ui.guard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.securityguard.app.models.InwardEntry
import com.securityguard.app.models.InwardEntryStatus
import com.securityguard.app.models.OutwardEntry
import com.securityguard.app.models.VisitorEntry
import com.securityguard.app.ui.components.DateTimePickerSheet
import com.securityguard.app.ui.components.EntryListCard
import com.securityguard.app.ui.components.GateFab
import com.securityguard.app.ui.components.StatusBadge
import com.securityguard.app.ui.components.StatusBanner
import com.securityguard.app.ui.theme.AppColors
import com.securityguard.app.ui.theme.AppSpacing
import com.securityguard.app.ui.theme.AppTypography
import com.securityguard.app.ui.theme.LocalPortalTheme
import com.securityguard.app.ui.theme.SecurityGuardTheme
import com.securityguard.app.viewmodel.GuardTab
import com.securityguard.app.viewmodel.GuardViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun GuardHomeScreen(
    onLogout: () -> Unit,
    onInwardDetail: (String) -> Unit,
    onOutwardDetail: (String) -> Unit,
    onVisitorDetail: (String) -> Unit,
    onNewEntry: () -> Unit,
    viewModel: GuardViewModel = hiltViewModel(),
) {
    val tab by viewModel.tab.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val inward by viewModel.inward.collectAsState()
    val outward by viewModel.outward.collectAsState()
    val visitors by viewModel.visitors.collectAsState()
    val theme = LocalPortalTheme.current

    SecurityGuardTheme {
        Scaffold(
            floatingActionButton = {
                if (tab == GuardTab.IN) {
                    GateFab(onClick = onNewEntry)
                }
            },
            bottomBar = {
                GuardBottomBar(selected = tab, onSelect = viewModel::selectTab)
            },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(theme.background),
            ) {
                GuardTopBar(tab = tab, onLogout = onLogout)
                Box(Modifier.weight(1f).fillMaxWidth()) {
                    if (loading) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    } else {
                        when (tab) {
                            GuardTab.IN -> InwardListContent(
                                inward?.entries.orEmpty(),
                                onClick = onInwardDetail,
                            )
                            GuardTab.OUT -> OutwardListContent(outward, onClick = onOutwardDetail)
                            GuardTab.VISITORS -> VisitorListContent(visitors, onClick = onVisitorDetail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GuardTopBar(tab: GuardTab, onLogout: () -> Unit) {
    val theme = LocalPortalTheme.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.BaseMargin, vertical = AppSpacing.Close),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Gate Control", style = AppTypography.Headline)
            Text(tab.label, style = AppTypography.Caption)
        }
        IconButton(onClick = onLogout) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = theme.primary)
        }
    }
}

@Composable
private fun GuardBottomBar(selected: GuardTab, onSelect: (GuardTab) -> Unit) {
    val theme = LocalPortalTheme.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(AppSpacing.Standard)
            .background(
                theme.surface,
                RoundedCornerShape(AppSpacing.RadiusXl),
            )
            .padding(vertical = AppSpacing.Tight),
    ) {
        GuardTab.entries.forEach { tab ->
            val active = tab == selected
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onSelect(tab) },
                    )
                    .padding(vertical = AppSpacing.Tight),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    tab.icon,
                    contentDescription = tab.label,
                    tint = if (active) theme.primary else AppColors.TextTertiary,
                )
                Text(
                    tab.label,
                    style = AppTypography.Caption,
                    color = if (active) theme.primary else AppColors.TextTertiary,
                )
            }
        }
    }
}

private val GuardTab.label: String
    get() = when (this) {
        GuardTab.IN -> "IN"
        GuardTab.OUT -> "OUT"
        GuardTab.VISITORS -> "Visitors"
    }

private val GuardTab.icon: ImageVector
    get() = when (this) {
        GuardTab.IN -> Icons.Default.Warehouse
        GuardTab.OUT -> Icons.Default.DirectionsCar
        GuardTab.VISITORS -> Icons.Default.People
    }

@Composable
private fun InwardListContent(entries: List<InwardEntry>, onClick: (String) -> Unit) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(AppSpacing.Standard),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Close),
    ) {
        items(entries, key = { it.id }) { entry ->
            EntryListCard(
                title = entry.truckRegistration,
                subtitle = "${entry.driverName} · ${entry.vendorName}",
                monoLine = entry.invoiceNumber.ifBlank { "—" },
                statusLabel = entry.status.displayName,
                onClick = { onClick(entry.id) },
            )
        }
    }
}

@Composable
private fun OutwardListContent(entries: List<OutwardEntry>, onClick: (String) -> Unit) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(AppSpacing.Standard),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Close),
    ) {
        items(entries, key = { it.id }) { entry ->
            EntryListCard(
                title = entry.truckRegistration,
                subtitle = entry.purpose,
                monoLine = entry.type.displayName,
                statusLabel = entry.status,
                statusColor = AppColors.WarningAmber,
                onClick = { onClick(entry.id) },
            )
        }
    }
}

@Composable
private fun VisitorListContent(entries: List<VisitorEntry>, onClick: (String) -> Unit) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(AppSpacing.Standard),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Close),
    ) {
        items(entries, key = { it.id }) { entry ->
            EntryListCard(
                title = entry.visitorName,
                subtitle = "${entry.company} → ${entry.hostName}",
                monoLine = entry.purpose,
                statusLabel = if (entry.inTime != null && entry.outTime == null) "Inside" else "Scheduled",
                statusColor = if (entry.ndaSigned) AppColors.SuccessGreen else AppColors.DangerRed,
                onClick = { onClick(entry.id) },
            )
        }
    }
}

@Composable
fun InwardDetailScreen(
    entryId: String,
    onBack: () -> Unit,
    viewModel: GuardViewModel = hiltViewModel(),
) {
    var entry by remember { mutableStateOf<InwardEntry?>(null) }
    var showAllowInPicker by remember { mutableStateOf(false) }
    var showExitPicker by remember { mutableStateOf(false) }

    LaunchedEffect(entryId) {
        viewModel.loadInwardDetail(entryId) { entry = it }
    }

    SecurityGuardTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(AppSpacing.Standard),
        ) {
            Text("Inward detail", style = AppTypography.Title2)
            entry?.let { e ->
                StatusBanner(
                    title = e.status.displayName,
                    body = bannerBody(e.status),
                    accent = statusAccent(e.status),
                )
                Spacer(Modifier.height(AppSpacing.Standard))
                StatusBadge(e.status)
                Spacer(Modifier.height(AppSpacing.Close))
                Text(e.truckRegistration, style = AppTypography.MonoHeadline)
                Text("Invoice: ${e.invoiceNumber}", style = AppTypography.MonoBody)
                Text("Driver: ${e.driverName}", style = AppTypography.Body)
                e.inTime?.let {
                    Text(
                        "IN: ${formatInstant(it)}",
                        style = AppTypography.MonoCaption,
                    )
                }
                Spacer(Modifier.height(AppSpacing.Section))
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Tight)) {
                    androidx.compose.material3.Button(onClick = { showAllowInPicker = true }) {
                        Text("Allow IN")
                    }
                    androidx.compose.material3.Button(
                        onClick = { showExitPicker = true },
                        enabled = !e.isExitLocked,
                    ) {
                        Text("Mark Exit")
                    }
                }
            } ?: CircularProgressIndicator()
            androidx.compose.material3.TextButton(onClick = onBack) { Text("Back") }
        }

        DateTimePickerSheet(
            visible = showAllowInPicker,
            title = "Allow IN — date & time",
            onDismiss = { showAllowInPicker = false },
            onConfirm = { instant ->
                viewModel.allowInInward(entryId, instant) { entry = it }
            },
        )
        DateTimePickerSheet(
            visible = showExitPicker,
            title = "Mark Exit — date & time",
            onDismiss = { showExitPicker = false },
            onConfirm = { instant ->
                viewModel.markExitInward(entryId, instant) { entry = it }
            },
        )
    }
}

@Composable
fun OutwardDetailScreen(entryId: String, onBack: () -> Unit) {
    var showPicker by remember { mutableStateOf(false) }
    val entry = remember { OutwardEntry.mock().copy(id = entryId) }

    SecurityGuardTheme {
        Column(Modifier.fillMaxSize().padding(AppSpacing.Standard)) {
            Text("Outward detail", style = AppTypography.Title2)
            Text(entry.truckRegistration, style = AppTypography.MonoHeadline)
            Text(entry.purpose, style = AppTypography.Body)
            Spacer(Modifier.height(AppSpacing.Section))
            androidx.compose.material3.Button(onClick = { showPicker = true }) { Text("Allow IN") }
            androidx.compose.material3.TextButton(onClick = onBack) { Text("Back") }
        }
        DateTimePickerSheet(
            visible = showPicker,
            title = "Allow IN — date & time",
            onDismiss = { showPicker = false },
            onConfirm = { },
        )
    }
}

@Composable
fun VisitorDetailScreen(entryId: String, onBack: () -> Unit) {
    var showPicker by remember { mutableStateOf(false) }
    val entry = remember { VisitorEntry.mock().copy(id = entryId) }

    SecurityGuardTheme {
        Column(Modifier.fillMaxSize().padding(AppSpacing.Standard)) {
            Text("Visitor detail", style = AppTypography.Title2)
            Text(entry.visitorName, style = AppTypography.MonoHeadline)
            Text("NDA signed: ${entry.ndaSigned}", style = AppTypography.Body)
            Spacer(Modifier.height(AppSpacing.Section))
            androidx.compose.material3.Button(
                onClick = { showPicker = true },
                enabled = entry.ndaSigned,
            ) { Text("Allow IN") }
            androidx.compose.material3.TextButton(onClick = onBack) { Text("Back") }
        }
        DateTimePickerSheet(
            visible = showPicker,
            title = "Allow IN — date & time",
            onDismiss = { showPicker = false },
            onConfirm = { },
        )
    }
}

@Composable
fun NewEntryPlaceholderScreen(onBack: () -> Unit) {
    SecurityGuardTheme {
        Column(
            Modifier.fillMaxSize().padding(AppSpacing.Standard),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("New entry", style = AppTypography.Title2)
            Text(
                "Regular inward / returnable return — wire to ML Kit document scanner + OCRService.",
                style = AppTypography.Body,
            )
            Spacer(Modifier.height(AppSpacing.Standard))
            AsyncImage(
                model = "https://placehold.co/600x400/EAF0FF/2454FF?text=Invoice+preview",
                contentDescription = "Invoice preview (Coil)",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.height(AppSpacing.Section))
            androidx.compose.material3.TextButton(onClick = onBack) { Text("Close") }
        }
    }
}

private fun bannerBody(status: InwardEntryStatus): String = when (status) {
    InwardEntryStatus.DRAFT -> "Complete vehicle and invoice details."
    InwardEntryStatus.PENDING_VERIFICATION -> "Awaiting stores verification."
    InwardEntryStatus.GRN_GENERATED -> "GRN ready — vehicle may exit after mark-exit."
    InwardEntryStatus.REJECTED -> "Entry rejected by stores."
    InwardEntryStatus.COMPLETED -> "Visit completed."
    else -> "Track lifecycle on this screen."
}

private fun statusAccent(status: InwardEntryStatus) = when (status) {
    InwardEntryStatus.GRN_GENERATED, InwardEntryStatus.COMPLETED -> AppColors.SuccessGreen
    InwardEntryStatus.REJECTED -> AppColors.DangerRed
    InwardEntryStatus.PENDING_VERIFICATION -> AppColors.WarningAmber
    else -> AppColors.GatePrimary
}

private fun formatInstant(instant: Instant): String =
    DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(instant)
