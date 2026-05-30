package com.securityguard.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class PortalTheme(
    val primary: Color,
    val primaryTint: Color,
    val background: Color,
    val surface: Color,
    val buttonShadow: Color,
)

val LocalPortalTheme = staticCompositionLocalOf {
    PortalTheme(
        primary = AppColors.GatePrimary,
        primaryTint = AppColors.GatePrimaryTint,
        background = AppColors.GateBackground,
        surface = AppColors.GateBlueSurface,
        buttonShadow = AppColors.GatePrimary.copy(alpha = 0.28f),
    )
}

object AppTheme {
    val Security = PortalTheme(
        primary = AppColors.GatePrimary,
        primaryTint = AppColors.GatePrimaryTint,
        background = AppColors.GateBackground,
        surface = AppColors.GateBlueSurface,
        buttonShadow = AppColors.GatePrimary.copy(alpha = 0.28f),
    )

    val Stores = PortalTheme(
        primary = AppColors.StoresPrimary,
        primaryTint = AppColors.StoresPrimaryTint,
        background = AppColors.StoresBackground,
        surface = AppColors.Surface,
        buttonShadow = AppColors.StoresPrimary.copy(alpha = 0.28f),
    )

    private val LightScheme = lightColorScheme(
        primary = AppColors.GatePrimary,
        onPrimary = Color.White,
        primaryContainer = AppColors.GatePrimaryContainer,
        secondary = AppColors.GateSecondaryBlue,
        background = AppColors.GateBackground,
        surface = AppColors.Surface,
        onSurface = AppColors.OnSurface,
        onSurfaceVariant = AppColors.OnSurfaceVariant,
        outline = AppColors.Outline,
        outlineVariant = AppColors.OutlineVariant,
        error = AppColors.DangerRed,
    )

    private val DarkScheme = darkColorScheme(
        primary = AppColors.GateSecondaryBlue,
        background = AppColors.OnSurface,
        surface = AppColors.GatePrimaryContainer,
    )
}

@Composable
fun SecurityGuardTheme(
    portalTheme: PortalTheme = AppTheme.Security,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) AppTheme.DarkScheme else AppTheme.LightScheme
    CompositionLocalProvider(LocalPortalTheme provides portalTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography.Material,
            content = content,
        )
    }
}
