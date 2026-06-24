package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val OmniPlayColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = OnSurfaceText,
    primaryContainer = PrimaryPurpleContainer,
    secondary = SecondaryCyan,
    onSecondary = OnSurfaceText,
    secondaryContainer = SecondaryCyanContainer,
    tertiary = TertiaryRose,
    background = DarkBackground,
    onBackground = OnSurfaceText,
    surface = DarkSurface,
    onSurface = OnSurfaceText,
    surfaceVariant = DarkSurfaceContainer,
    onSurfaceVariant = OnSurfaceVariantText,
    outline = OutlineColor,
    outlineVariant = OutlineVariantColor
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OmniPlayColorScheme,
        typography = Typography,
        content = content
    )
}
