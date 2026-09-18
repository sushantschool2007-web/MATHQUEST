package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    xp: Int? = null,
    hearts: Int? = null,
    streak: Int? = null,
    customActions: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            }
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = QuestTextPrimary
                    )
                }
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(end = 12.dp)
            ) {
                customActions?.invoke(this)

                // Adaptive Theme Toggle Switch (Sun/Moon icon)
                IconButton(
                    onClick = { ThemeController.toggleTheme() },
                    modifier = Modifier.testTag("theme_toggle_button")
                ) {
                    Icon(
                        imageVector = if (ThemeController.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (ThemeController.isDarkTheme) "Switch to Light Mode" else "Switch to Dark Mode",
                        tint = if (ThemeController.isDarkTheme) Color(0xFFFBBF24) else Color(0xFF4F46E5)
                    )
                }

                if (hearts != null) {
                    Surface(
                        color = QuestErrorRed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestErrorRed.copy(alpha = 0.4f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("❤️", fontSize = 12.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "$hearts",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = QuestErrorRed
                            )
                        }
                    }
                }

                if (streak != null && streak > 0) {
                    Surface(
                        color = QuestAccentAmber.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestAccentAmber.copy(alpha = 0.4f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("🔥", fontSize = 12.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "$streak",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = QuestAccentAmber
                            )
                        }
                    }
                }

                if (xp != null) {
                    Surface(
                        color = QuestAccentGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestAccentGold.copy(alpha = 0.4f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("⚡", fontSize = 12.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "$xp XP",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = QuestAccentGoldLight
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = QuestNavyDark,
            titleContentColor = QuestTextPrimary
        ),
        modifier = modifier
    )
}

@Composable
fun QuestMetricCard(
    label: String,
    value: String,
    icon: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = icon, fontSize = 18.sp)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = QuestTextPrimary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = QuestTextSecondary
            )
        }
    }
}

@Composable
fun OptionCard(
    optionLetter: String, // "A", "B", "C", "D"
    optionText: String,
    isSelected: Boolean,
    isAnswerRevealed: Boolean,
    isCorrectOption: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = ThemeController.isDarkTheme

    // Light Mode: Border outline with subtle blue tint on selection
    // Dark Mode: Glowing border with dark indigo background fill on selection
    val backgroundColor = when {
        isAnswerRevealed && isCorrectOption -> (if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess).copy(alpha = 0.2f)
        isAnswerRevealed && isSelected && !isCorrectOption -> (if (isDark) DarkSpectrumError else LightSpectrumError).copy(alpha = 0.2f)
        isSelected -> {
            if (isDark) DarkSpectrumActionElectric.copy(alpha = 0.28f)
            else Color(0xFFEFF6FF) // subtle blue tint in Light Mode
        }
        else -> if (isDark) DarkSpectrumCard else LightSpectrumCard
    }

    val borderColor = when {
        isAnswerRevealed && isCorrectOption -> if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess
        isAnswerRevealed && isSelected && !isCorrectOption -> if (isDark) DarkSpectrumError else LightSpectrumError
        isSelected -> {
            if (isDark) DarkSpectrumActionElectric // glowing border in Dark Mode
            else LightSpectrumActionPrimary // border outline in Light Mode
        }
        else -> if (isDark) DarkSpectrumBorder else LightSpectrumBorder
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(enabled = !isAnswerRevealed, onClick = onClick)
            .testTag("option_$optionLetter"),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected || (isAnswerRevealed && isCorrectOption)) borderColor
                        else (if (isDark) DarkSpectrumSurface else LightSpectrumSurface)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = optionLetter,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected || (isAnswerRevealed && isCorrectOption)) Color.White
                    else (if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary)
                )
            }

            Spacer(Modifier.width(14.dp))

            Text(
                text = optionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (isAnswerRevealed) {
                if (isCorrectOption) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Correct",
                        tint = QuestSuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Incorrect",
                        tint = QuestErrorRed,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TeachMeDialog(
    given: String,
    weNeed: String,
    formula: String,
    substitution: String,
    calculation: String,
    finalAnswer: String,
    shortcut: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎓", fontSize = 24.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Teach Me Step-by-Step",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TeachMeSection("Given:", given, QuestTextSecondary)
                TeachMeSection("We Need:", weNeed, QuestTextSecondary)
                TeachMeSection("Formula:", formula, QuestPrimaryBlueLight)
                TeachMeSection("Substitution:", substitution, QuestTextPrimary)
                TeachMeSection("Calculation:", calculation, QuestTextPrimary)
                TeachMeSection("Final Answer:", finalAnswer, QuestSuccessGreen)

                if (shortcut.isNotBlank()) {
                    Surface(
                        color = QuestAccentGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestAccentGold.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                "⚡ CET Shortcut:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestAccentGoldLight
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                shortcut,
                                style = MaterialTheme.typography.bodySmall,
                                color = QuestTextPrimary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Got It!")
            }
        },
        containerColor = QuestNavySurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun TeachMeSection(header: String, body: String, textColor: Color) {
    if (body.isBlank()) return
    Column {
        Text(
            text = header,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = QuestTextTertiary
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = textColor
        )
    }
}

@Composable
fun HintDialog(
    hints: List<String>,
    unlockedHintCount: Int,
    onUnlockNextHint: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡", fontSize = 24.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Hints (${unlockedHintCount}/${hints.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Unlocking hints may slightly reduce the XP earned for this question.",
                    style = MaterialTheme.typography.bodySmall,
                    color = QuestTextSecondary
                )

                hints.forEachIndexed { index, hintText ->
                    val isUnlocked = index < unlockedHintCount
                    val hintTitle = when (index) {
                        0 -> "Hint 1: Concept Hint"
                        1 -> "Hint 2: Formula Hint"
                        else -> "Hint 3: Method Hint"
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUnlocked) QuestNavyCard else QuestNavyDark.copy(alpha = 0.5f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isUnlocked) QuestAccentGold.copy(alpha = 0.5f) else QuestNavyBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = hintTitle,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isUnlocked) QuestAccentGoldLight else QuestTextTertiary
                                )
                                if (!isUnlocked) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = QuestTextTertiary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            if (isUnlocked) {
                                Text(
                                    text = hintText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuestTextPrimary
                                )
                            } else {
                                Text(
                                    text = "Tap 'Unlock Next Hint' to reveal.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuestTextTertiary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (unlockedHintCount < hints.size) {
                Button(
                    onClick = onUnlockNextHint,
                    colors = ButtonDefaults.buttonColors(containerColor = QuestAccentGold),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Unlock Next Hint (-2 XP)", color = Color.Black)
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Close")
                }
            }
        },
        dismissButton = {
            if (unlockedHintCount < hints.size) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = QuestTextSecondary)
                }
            }
        },
        containerColor = QuestNavySurface,
        shape = RoundedCornerShape(20.dp)
    )
}
