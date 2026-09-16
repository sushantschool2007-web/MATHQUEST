package com.example.ui.gamemodes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.GamificationConfig
import com.example.data.repository.MathRepository
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    repository: MathRepository,
    onBack: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val prefs by repository.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = null)
    val userProfile by repository.latestUserProfile.collectAsStateWithLifecycle(initialValue = null)
    val levelInfo = GamificationConfig.getLevelForXp(prefs?.totalXp ?: 50)
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val studentDisplayName = userProfile?.displayName?.takeIf { it.isNotBlank() }
        ?: prefs?.studentName?.takeIf { it.isNotBlank() }
        ?: "Aspirant"
    val studentEmail = userProfile?.email?.takeIf { it.isNotBlank() }
        ?: prefs?.userEmail?.takeIf { it.isNotBlank() }
        ?: "aspirant@cetquest.edu"

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "Aspirant Profile",
                onBack = onBack,
                customActions = {
                    IconButton(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.testTag("profile_logout_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Log Out",
                            tint = QuestErrorRed
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar & Rank Card
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(QuestPrimaryBlue, QuestAccentGold))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(levelInfo.rankBadge, fontSize = 36.sp)
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = studentDisplayName,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Level ${levelInfo.levelNumber} — ${levelInfo.title}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = QuestAccentGoldLight
                    )

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        color = QuestPrimaryBlue.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "MHT-CET ${prefs?.targetYear ?: "2026"} Aspirant",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = QuestPrimaryBlueLight,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Prep Target Info
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "PREPARATION ROADMAP",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextTertiary
                    )

                    ProfileItemRow("Current Prep Tier", prefs?.prepLevel ?: "Developing")
                    ProfileItemRow("Daily Study Goal", "${prefs?.dailyGoalMinutes ?: 30} minutes / day")
                    ProfileItemRow("Target Maths Score", "${prefs?.targetScore ?: 90} / 100 Marks")
                    ProfileItemRow("Active Study Streak", "${prefs?.streakDays ?: 1} Days Active 🔥")
                    ProfileItemRow("Total Experience", "${prefs?.totalXp ?: 50} XP ⚡")
                }
            }

            // Firebase Auth Account Card
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "ACCOUNT & SESSION",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextTertiary
                    )

                    ProfileItemRow("Logged-in Email", studentEmail)
                    ProfileItemRow("Account Status", "Active & Synchronized")

                    Spacer(Modifier.height(4.dp))

                    OutlinedButton(
                        onClick = { showLogoutDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = QuestErrorRed
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestErrorRed.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = QuestErrorRed
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Log Out",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // App Identity & Offline Notice
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavySurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "ABOUT CET MATH QUEST",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextTertiary
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Designed specifically for Maharashtra State MHT-CET PCM Mathematics. Complete local offline storage powered by Room Database & Jetpack Compose.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = QuestNavyCard,
            title = {
                Text(
                    "Confirm Logout",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            },
            text = {
                Text(
                    "Are you sure you want to log out of CET Math Quest? Your offline progress and mock scores will remain saved on this device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = QuestTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuestErrorRed,
                        contentColor = QuestNavyDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("confirm_logout_button")
                ) {
                    Text("Log Out", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = QuestTextSecondary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = QuestTextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = QuestTextPrimary)
    }
}
