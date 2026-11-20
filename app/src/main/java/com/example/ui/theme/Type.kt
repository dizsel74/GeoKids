package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

val RubikFontFamily = FontFamily(
    Font(R.font.rubik, FontWeight.Normal),
    Font(R.font.rubik_medium, FontWeight.Medium),
    Font(R.font.rubik_semibold, FontWeight.SemiBold),
)

private val DefaultTypography = Typography()

val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.SemiBold),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.Medium),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.Medium),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.Medium),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = RubikFontFamily, fontWeight = FontWeight.Medium),
)
