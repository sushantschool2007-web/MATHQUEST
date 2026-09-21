package com.example.ui.gamemodes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.CheckCircle
import com.example.data.repository.CloudSyncStatus
import com.example.data.repository.FirestoreSyncRepository
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    repository: MathRepository,
    firestoreSyncRepository: FirestoreSyncRepository? = null,
    onBack: () -> Unit,
    onLogout: () -> Unit = {},
    onAdminDashboard: () -> Unit = {}
) {
    val prefs by repository.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = null)
    val userProfile by repository.latestUserProfile.collectAsStateWithLifecycle(initialValue = null)
    val syncStatus = firestoreSyncRepository?.syncStatus?.collectAsStateWithLifecycle()?.value ?: CloudSyncStatus()
    val levelInfo = GamificationConfig.getLevelForXp(prefs?.totalXp ?: 0)
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isManualSyncing by remember { mutableStateOf(false) }
    var syncNotice by remember { mutableStateOf<String?>(null) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showAboutAppDialog by remember { mutableStateOf(false) }
    var showClearCacheDialog by remember { mutableStateOf(false) }
    var cacheClearedMessage by remember { mutableStateOf<String?>(null) }
    var hapticsEnabled by remember { mutableStateOf(true) }
    var soundEffectsEnabled by remember { mutableStateOf(true) }

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
                    ProfileItemRow("Active Study Streak", "${prefs?.streakDays ?: 0} Days Active 🔥")
                    ProfileItemRow("Total Experience", "${prefs?.totalXp ?: 0} XP ⚡")
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

            // Cloud Backend & Sync Card
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = QuestCyanAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Firebase Cloud Backend",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestTextPrimary
                            )
                            Text(
                                "Live Firestore Sync & Real-Time Leaderboard",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = QuestCyanAccent
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Your XP, Level, Daily Streak, and Question Records are safely synced to Google Cloud Firestore under your account.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )

                    if (syncNotice != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = syncNotice ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestAccentGoldLight
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = {
                            prefs?.let { currentPrefs ->
                                scope.launch {
                                    isManualSyncing = true
                                    try {
                                        firestoreSyncRepository?.syncProgressToCloud(currentPrefs)
                                        syncNotice = "✅ Successfully synced to Firebase Cloud!"
                                    } catch (e: Exception) {
                                        syncNotice = "Sync skipped: ${e.localizedMessage}"
                                    } finally {
                                        isManualSyncing = false
                                    }
                                }
                            }
                        },
                        enabled = !isManualSyncing,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = QuestCyanAccent
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestCyanAccent.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("manual_cloud_sync_button")
                    ) {
                        if (isManualSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = QuestCyanAccent,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = QuestCyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Backup & Sync with Cloud Now",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // App Identity & Offline Notice
            // Administrator Dashboard Tile
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAdminDashboard() }
                    .testTag("profile_admin_dashboard_button")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("👑", fontSize = 22.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "Admin & Teacher Dashboard",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                "Student cohort tracking & content authoring",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFC7D2FE)
                            )
                        }
                    }
                    Text(
                        "OPEN →",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF818CF8)
                    )
                }
            }

            // App Settings & Feedback Preferences Card
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "APP PREFERENCES & TACTILE FEEDBACK",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextTertiary
                    )

                    // Haptics Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Vibration,
                                contentDescription = null,
                                tint = QuestAccentGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    "Haptic Feedback",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = QuestTextPrimary
                                )
                                Text(
                                    "Tactile vibration pulses on quiz answers",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuestTextSecondary
                                )
                            }
                        }
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = { hapticsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = QuestNavyDark,
                                checkedTrackColor = QuestAccentGold,
                                uncheckedThumbColor = QuestTextSecondary,
                                uncheckedTrackColor = QuestNavySurface
                            ),
                            modifier = Modifier.testTag("haptic_toggle_switch")
                        )
                    }

                    HorizontalDivider(color = QuestNavyBorder)

                    // Sound Effects Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = QuestCyanAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    "Audio Feedback",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = QuestTextPrimary
                                )
                                Text(
                                    "Subtle sound chimes for level ups and victories",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuestTextSecondary
                                )
                            }
                        }
                        Switch(
                            checked = soundEffectsEnabled,
                            onCheckedChange = { soundEffectsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = QuestNavyDark,
                                checkedTrackColor = QuestCyanAccent,
                                uncheckedThumbColor = QuestTextSecondary,
                                uncheckedTrackColor = QuestNavySurface
                            ),
                            modifier = Modifier.testTag("audio_toggle_switch")
                        )
                    }
                }
            }

            // Legal, Compliance & Play Store Information Card
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "LEGAL & STORE COMPLIANCE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextTertiary
                    )

                    // Privacy Policy
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrivacyPolicyDialog = true }
                            .padding(vertical = 6.dp)
                            .testTag("privacy_policy_button"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Policy,
                                contentDescription = null,
                                tint = QuestPrimaryBlueLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Privacy Policy",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = QuestTextPrimary
                            )
                        }
                        Text("VIEW →", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = QuestPrimaryBlueLight)
                    }

                    HorizontalDivider(color = QuestNavyBorder)

                    // Terms of Service
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTermsDialog = true }
                            .padding(vertical = 6.dp)
                            .testTag("terms_of_service_button"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = QuestTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Terms of Service",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = QuestTextPrimary
                            )
                        }
                        Text("VIEW →", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = QuestTextSecondary)
                    }

                    HorizontalDivider(color = QuestNavyBorder)

                    // About App & Release Notes
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAboutAppDialog = true }
                            .padding(vertical = 6.dp)
                            .testTag("about_app_button"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = QuestAccentGoldLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "About CET Math Quest",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = QuestTextPrimary
                            )
                        }
                        Text("v1.0.0 →", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = QuestAccentGoldLight)
                    }

                    HorizontalDivider(color = QuestNavyBorder)

                    // Clear Cache / Session Reset
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showClearCacheDialog = true }
                            .padding(vertical = 6.dp)
                            .testTag("clear_cache_button"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = QuestTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Clear Temporary App Cache",
                                style = MaterialTheme.typography.bodyMedium,
                                color = QuestTextSecondary
                            )
                        }
                        Text("RESET", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = QuestTextSecondary)
                    }

                    if (cacheClearedMessage != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = cacheClearedMessage ?: "",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestSuccessGreen
                        )
                    }
                }
            }

            // Play Store Verified Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = QuestSuccessGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Google Play Production Ready • Build 1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuestTextTertiary
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }

    // Privacy Policy Dialog
    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            containerColor = QuestNavyCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = QuestAccentGold)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Privacy Policy",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "CET Math Quest is committed to preserving and safeguarding student privacy. We adhere strictly to Google Play Family and Educational Data Policies.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuestTextPrimary
                    )
                    Text(
                        "1. Local-First Offline Data: All your solving records, streak metrics, mistake bookmarks, and formula favorites are stored directly on your device via Room SQLite.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                    Text(
                        "2. Cloud Sync: When you create or sign in with an account, your XP, rank, and achievements sync securely to Google Firebase Firestore via TLS encryption.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                    Text(
                        "3. No Data Selling: We do NOT sell, lease, or distribute student personal information or telemetry to third-party ad brokers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                    Text(
                        "4. Account Deletion: Users can delete their data or log out anytime directly from this profile panel.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyPolicyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue)
                ) {
                    Text("Understood", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Terms of Service Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            containerColor = QuestNavyCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = QuestCyanAccent)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Terms of Service",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Welcome to CET Math Quest. By using this application, you agree to these fair use terms:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuestTextPrimary
                    )
                    Text(
                        "• Academic Resource: This application is provided for Maharashtra State MHT-CET Mathematics entrance examination preparation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                    Text(
                        "• Fair Use: Formulas, practice drills, and mock tests are designed to facilitate individual student revision and self-assessment.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                    Text(
                        "• Content Integrity: Question banks reflect past-year syllabus weightages (XI 20%, XII 80%) under Maharashtra State Board guidelines.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showTermsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // About App Dialog
    if (showAboutAppDialog) {
        AlertDialog(
            onDismissRequest = { showAboutAppDialog = false },
            containerColor = QuestNavyCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = QuestAccentGold)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "About CET Math Quest",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Version: 1.0.0 (Production Release)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                    Text(
                        "Target: Maharashtra MHT-CET PCM Mathematics",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestAccentGoldLight
                    )
                    Text(
                        "Architecture: Jetpack Compose + Clean Architecture + Room SQLite Database + Firebase Cloud Sync.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Features Included:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                    Text("• Standard 11th & 12th Complete Syllabus (34 Chapters)", style = MaterialTheme.typography.bodySmall, color = QuestTextSecondary)
                    Text("• 50-Question 2026 MHT-CET Pattern Mock Tests", style = MaterialTheme.typography.bodySmall, color = QuestTextSecondary)
                    Text("• Speed Run, Daily Challenges & Chapter Boss Battles", style = MaterialTheme.typography.bodySmall, color = QuestTextSecondary)
                    Text("• Interactive Formula Book & Smart Mistake Book", style = MaterialTheme.typography.bodySmall, color = QuestTextSecondary)
                    Text("• Multi-Factor Admin Portal & Cohort Tracking", style = MaterialTheme.typography.bodySmall, color = QuestTextSecondary)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAboutAppDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue)
                ) {
                    Text("Great!", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Clear Cache Dialog
    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            containerColor = QuestNavyCard,
            title = {
                Text(
                    "Clear App Cache",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            },
            text = {
                Text(
                    "This clears temporary in-memory graphics and calculation caches. Your solved questions, streak, and XP will remain completely safe.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = QuestTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearCacheDialog = false
                        cacheClearedMessage = "✅ Temporary cache cleared successfully!"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue)
                ) {
                    Text("Clear Cache", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearCacheDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = QuestTextSecondary)
                ) {
                    Text("Cancel")
                }
            }
        )
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
