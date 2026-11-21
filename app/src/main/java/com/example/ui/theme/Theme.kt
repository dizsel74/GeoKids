package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GeoOrange,
    onPrimary = Color.White,
    primaryContainer = GeoNavy,
    onPrimaryContainer = Color.White,
    secondary = GeoMint,
    onSecondary = GeoNavy,
    secondaryContainer = GeoNavy,
    onSecondaryContainer = GeoMint,
    background = GeoNavy,
    onBackground = Color.White,
    surface = GeoNavy,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = GeoOrange,
    onPrimary = Color.White,
    primaryContainer = GeoOrangeSoft,
    onPrimaryContainer = GeoNavy,
    secondary = GeoMint,
    onSecondary = GeoNavy,
    secondaryContainer = GeoMintCard,
    onSecondaryContainer = GeoNavy,
    tertiary = GeoNavy,
    onTertiary = Color.White,
    background = GeoMintSurface,
    onBackground = GeoNavy,
    surface = GeoSurfaceWhite,
    onSurface = GeoNavy,
    surfaceVariant = GeoMintCard,
    onSurfaceVariant = GeoNavyMuted,
    outline = GeoMintBorder,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
