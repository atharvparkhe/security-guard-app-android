package com.securityguard.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.securityguard.app.R

object AppTypography {
    val Inter = FontFamily(
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold),
    )

    val JetBrainsMono = FontFamily(
        Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
        Font(R.font.jetbrains_mono_semibold, FontWeight.SemiBold),
    )

    val Display = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        color = AppColors.TextPrimary,
    )

    val Title1 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = AppColors.TextPrimary,
    )

    val Title2 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        color = AppColors.TextPrimary,
    )

    val Headline = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        color = AppColors.TextPrimary,
    )

    val Body = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        color = AppColors.TextPrimary,
    )

    val Callout = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        color = AppColors.TextSecondary,
    )

    val Subheadline = TextStyle(
        fontFamily = Inter,
        fontSize = 15.sp,
        color = AppColors.TextSecondary,
    )

    val Footnote = TextStyle(
        fontFamily = Inter,
        fontSize = 13.sp,
        color = AppColors.TextTertiary,
    )

    val Caption = TextStyle(
        fontFamily = Inter,
        fontSize = 12.sp,
        color = AppColors.TextTertiary,
    )

    val LabelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        color = AppColors.TextSecondary,
    )

    val MonoHeadline = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = AppColors.TextPrimary,
    )

    val MonoBody = TextStyle(
        fontFamily = JetBrainsMono,
        fontSize = 15.sp,
        color = AppColors.TextPrimary,
    )

    val MonoCaption = TextStyle(
        fontFamily = JetBrainsMono,
        fontSize = 12.sp,
        color = AppColors.TextSecondary,
    )

    val Material = Typography(
        displayLarge = Display,
        headlineLarge = Title1,
        headlineMedium = Title2,
        titleLarge = Headline,
        bodyLarge = Body,
        bodyMedium = Callout,
        labelMedium = LabelMedium,
        labelSmall = Caption,
    )
}
