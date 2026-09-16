package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = QuestPrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = QuestPrimaryBlue.copy(alpha = 0.25f),
    onPrimaryContainer = Color.White,
    secondary = QuestAccentGold,
    onSecondary = Color.Black,
    secondaryContainer = QuestAccentGold.copy(alpha = 0.2f),
    onSecondaryContainer = QuestAccentGoldLight,
    tertiary = QuestSuccessGreen,
    onTertiary = Color.Black,
    tertiaryContainer = QuestSuccessGreen.copy(alpha = 0.2f),
    onTertiaryContainer = QuestSuccessGreen,
    background = QuestNavyDark,
    onBackground = QuestTextPrimary,
    surface = QuestNavySurface,
    onSurface = QuestTextPrimary,
    surfaceVariant = QuestNavyCard,
    onSurfaceVariant = QuestTextSecondary,
    outline = QuestNavyBorder,
    error = QuestErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = QuestPrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = QuestPrimaryBlue.copy(alpha = 0.15f),
    onPrimaryContainer = QuestPrimaryBlue,
    secondary = QuestAccentAmber,
    onSecondary = Color.White,
    secondaryContainer = QuestAccentGold.copy(alpha = 0.2f),
    onSecondaryContainer = QuestAccentAmber,
    tertiary = QuestSuccessGreenDark,
    onTertiary = Color.White,
    tertiaryContainer = QuestSuccessGreen.copy(alpha = 0.15f),
    onTertiaryContainer = QuestSuccessGreenDark,
    background = QuestLightBg,
    onBackground = QuestLightTextPrimary,
    surface = QuestLightSurface,
    onSurface = QuestLightTextPrimary,
    surfaceVariant = QuestLightCard,
    onSurfaceVariant = QuestLightTextSecondary,
    outline = QuestLightBorder,
    error = QuestErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark gaming theme for immersive focus
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
