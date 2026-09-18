package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// --- Global Theme Controller ---
object ThemeController {
    var isDarkTheme by mutableStateOf(true)

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}

// ==========================================
// LIGHT MODE SPECTRUM (Specified by Design System)
// ==========================================
// Background: Pure white (#FFFFFF)
val LightSpectrumBackground = Color(0xFFFFFFFF)
// Secondary cards / containers: Soft gray (#F1F5F9 / #F8F9FA)
val LightSpectrumCard = Color(0xFFF1F5F9)
val LightSpectrumSurface = Color(0xFFF8F9FA)
val LightSpectrumBorder = Color(0xFFE2E8F0)
// Text hierarchy: Deep slate / Charcoal (#0F172A / #1E293B)
val LightSpectrumTextPrimary = Color(0xFF0F172A)
val LightSpectrumTextSecondary = Color(0xFF64748B)
val LightSpectrumTextTertiary = Color(0xFF94A3B8)
// Actions: Royal Blue / Vivid Indigo (#2563EB / #4F46E5)
val LightSpectrumActionPrimary = Color(0xFF2563EB)
val LightSpectrumActionIndigo = Color(0xFF4F46E5)
// Accents: Emerald Green (#16A34A / #10B981)
val LightSpectrumSuccess = Color(0xFF16A34A)
val LightSpectrumEmerald = Color(0xFF10B981)
val LightSpectrumError = Color(0xFFDC2626)

// ==========================================
// DARK MODE SPECTRUM (Specified by Design System)
// ==========================================
// Background: Deep midnight / Dark Slate (#0F172A)
val DarkSpectrumBackground = Color(0xFF0F172A)
// Surface / Elevated Cards: Elevated Slate (#1E293B) with 1px border (#334155)
val DarkSpectrumCard = Color(0xFF1E293B)
val DarkSpectrumSurface = Color(0xFF1E293B)
val DarkSpectrumBorder = Color(0xFF334155)
// Text hierarchy: High-contrast bright off-white (#F8FAFC)
val DarkSpectrumTextPrimary = Color(0xFFF8FAFC)
val DarkSpectrumTextSecondary = Color(0xFF94A3B8)
val DarkSpectrumTextTertiary = Color(0xFF64748B)
// Actions: Electric Violet / Electric Indigo (#6366F1)
val DarkSpectrumActionPrimary = Color(0xFF6366F1)
val DarkSpectrumActionElectric = Color(0xFF6366F1)
// Accents: Bright Cyan (#06B6D4) & Emerald
val DarkSpectrumCyan = Color(0xFF06B6D4)
val DarkSpectrumSuccess = Color(0xFF10B981)
val DarkSpectrumError = Color(0xFFDC2626)
val DarkSpectrumAmber = Color(0xFFF59E0B)

// ==========================================
// DYNAMIC ADAPTIVE ACCESSORS (Seamless Light/Dark Switching)
// ==========================================
val AppBackground: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumBackground else LightSpectrumBackground

val AppCardSurface: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumCard else LightSpectrumCard

val AppCardBorder: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumBorder else LightSpectrumBorder

val AppTextPrimary: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumTextPrimary else LightSpectrumTextPrimary

val AppTextSecondary: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumTextSecondary else LightSpectrumTextSecondary

val AppTextTertiary: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumTextTertiary else LightSpectrumTextTertiary

val AppActionPrimary: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumActionPrimary else LightSpectrumActionPrimary

val AppSuccessGreen: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumSuccess else LightSpectrumSuccess

val AppErrorRed: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumError else LightSpectrumError

val AppAccentCyanOrBlue: Color
    get() = if (ThemeController.isDarkTheme) DarkSpectrumCyan else LightSpectrumActionIndigo

// Backward-compatible aliases mapped to the new precise spectrum
val QuestNavyDark: Color get() = AppBackground
val QuestNavySurface: Color get() = AppCardSurface
val QuestNavyCard: Color get() = AppCardSurface
val QuestNavyBorder: Color get() = AppCardBorder
val QuestPrimaryBlue: Color get() = AppActionPrimary
val QuestPrimaryBlueLight: Color get() = if (ThemeController.isDarkTheme) Color(0xFF818CF8) else Color(0xFF3B82F6)
val QuestCyanAccent: Color get() = if (ThemeController.isDarkTheme) DarkSpectrumCyan else LightSpectrumActionIndigo
val QuestAccentGold: Color get() = if (ThemeController.isDarkTheme) Color(0xFFFBBF24) else Color(0xFFD97706)
val QuestAccentGoldLight: Color get() = if (ThemeController.isDarkTheme) Color(0xFFFDE68A) else Color(0xFFB45309)
val QuestAccentAmber: Color get() = Color(0xFFF59E0B)
val QuestSuccessGreen: Color get() = AppSuccessGreen
val QuestSuccessGreenDark: Color get() = Color(0xFF059669)
val QuestErrorRed: Color get() = AppErrorRed
val QuestErrorRedDark: Color get() = Color(0xFFB91C1C)
val QuestWarningOrange: Color get() = Color(0xFFF97316)
val QuestTextPrimary: Color get() = AppTextPrimary
val QuestTextSecondary: Color get() = AppTextSecondary
val QuestTextTertiary: Color get() = AppTextTertiary
val QuestLightBg: Color get() = LightSpectrumBackground
val QuestLightSurface: Color get() = LightSpectrumSurface
val QuestLightCard: Color get() = LightSpectrumCard
val QuestLightBorder: Color get() = LightSpectrumBorder
val QuestLightTextPrimary: Color get() = LightSpectrumTextPrimary
val QuestLightTextSecondary: Color get() = LightSpectrumTextSecondary
