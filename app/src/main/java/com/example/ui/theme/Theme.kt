package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkSpectrumActionPrimary, // Electric Indigo #6366F1
    onPrimary = Color.White,
    primaryContainer = DarkSpectrumActionPrimary.copy(alpha = 0.25f),
    onPrimaryContainer = DarkSpectrumTextPrimary,
    secondary = DarkSpectrumCyan, // Bright Cyan #06B6D4
    onSecondary = Color.Black,
    secondaryContainer = DarkSpectrumCyan.copy(alpha = 0.2f),
    onSecondaryContainer = DarkSpectrumCyan,
    tertiary = DarkSpectrumSuccess, // Emerald #10B981
    onTertiary = Color.Black,
    tertiaryContainer = DarkSpectrumSuccess.copy(alpha = 0.2f),
    onTertiaryContainer = DarkSpectrumSuccess,
    background = DarkSpectrumBackground, // Deep midnight #0F172A
    onBackground = DarkSpectrumTextPrimary, // Off-white #F8FAFC
    surface = DarkSpectrumSurface, // Elevated Slate #1E293B
    onSurface = DarkSpectrumTextPrimary,
    surfaceVariant = DarkSpectrumCard,
    onSurfaceVariant = DarkSpectrumTextSecondary, // Muted Slate #94A3B8
    outline = DarkSpectrumBorder, // 1px border #334155
    error = DarkSpectrumError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = LightSpectrumActionPrimary, // Royal Blue #2563EB / Vivid Indigo #4F46E5
    onPrimary = Color.White,
    primaryContainer = LightSpectrumActionPrimary.copy(alpha = 0.12f),
    onPrimaryContainer = LightSpectrumActionPrimary,
    secondary = LightSpectrumActionIndigo,
    onSecondary = Color.White,
    secondaryContainer = LightSpectrumActionIndigo.copy(alpha = 0.15f),
    onSecondaryContainer = LightSpectrumActionIndigo,
    tertiary = LightSpectrumSuccess, // Emerald Green #16A34A
    onTertiary = Color.White,
    tertiaryContainer = LightSpectrumSuccess.copy(alpha = 0.15f),
    onTertiaryContainer = LightSpectrumSuccess,
    background = LightSpectrumBackground, // Pure white #FFFFFF
    onBackground = LightSpectrumTextPrimary, // Dark Charcoal #0F172A
    surface = LightSpectrumSurface, // Soft gray #F8F9FA
    onSurface = LightSpectrumTextPrimary,
    surfaceVariant = LightSpectrumCard, // Soft gray #F1F5F9
    onSurfaceVariant = LightSpectrumTextSecondary, // Slate gray #64748B
    outline = LightSpectrumBorder,
    error = LightSpectrumError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = ThemeController.isDarkTheme,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
