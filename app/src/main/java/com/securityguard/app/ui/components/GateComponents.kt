package com.securityguard.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.securityguard.app.models.InwardEntryStatus
import com.securityguard.app.ui.theme.AppColors
import com.securityguard.app.ui.theme.AppSpacing
import com.securityguard.app.ui.theme.AppTypography
import com.securityguard.app.ui.theme.LocalPortalTheme

@Composable
fun StatusBadge(status: InwardEntryStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        InwardEntryStatus.COMPLETED, InwardEntryStatus.GRN_GENERATED ->
            AppColors.SuccessTint to AppColors.SuccessGreen
        InwardEntryStatus.REJECTED -> AppColors.DangerTint to AppColors.DangerRed
        InwardEntryStatus.PENDING_VERIFICATION, InwardEntryStatus.ACKNOWLEDGED ->
            AppColors.WarningTint to AppColors.WarningAmber
        else -> AppColors.NeutralTint to AppColors.NeutralGray
    }
    Text(
        text = status.displayName,
        style = AppTypography.LabelMedium,
        color = fg,
        modifier = modifier
            .clip(RoundedCornerShape(AppSpacing.RadiusSm))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
fun EntryListCard(
    title: String,
    subtitle: String,
    monoLine: String,
    statusLabel: String,
    statusColor: Color = AppColors.GatePrimary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppSpacing.RadiusMd),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(AppSpacing.Standard)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(title, style = AppTypography.Headline)
                Text(
                    statusLabel,
                    style = AppTypography.Caption,
                    color = statusColor,
                )
            }
            Spacer(Modifier.height(AppSpacing.Tight))
            Text(subtitle, style = AppTypography.Subheadline)
            Spacer(Modifier.height(AppSpacing.Micro))
            Text(monoLine, style = AppTypography.MonoBody)
        }
    }
}

@Composable
fun GateFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val theme = LocalPortalTheme.current
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(AppSpacing.FabSize),
        containerColor = theme.primary,
        contentColor = Color.White,
        shape = CircleShape,
    ) {
        Icon(Icons.Default.Add, contentDescription = "New entry")
    }
}

@Composable
fun StatusBanner(title: String, body: String, accent: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppSpacing.RadiusMd),
        color = accent.copy(alpha = 0.12f),
    ) {
        Column(Modifier.padding(AppSpacing.Standard)) {
            Text(title, style = AppTypography.Headline, color = accent)
            Spacer(Modifier.height(AppSpacing.Micro))
            Text(body, style = AppTypography.Callout)
        }
    }
}

@Composable
fun OtpInputRow(otp: String, length: Int = 6) {
    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Tight)) {
        repeat(length) { index ->
            val char = otp.getOrNull(index)?.toString() ?: ""
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(AppSpacing.RadiusSm))
                    .background(AppColors.SurfaceContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(char, style = AppTypography.MonoHeadline)
            }
        }
    }
}
