package com.example.ui.gamemodes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.FirestoreSyncRepository
import com.example.data.repository.MathRepository
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val city: String,
    val xp: Int,
    val solved: Int,
    val isUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    repository: MathRepository,
    firestoreSyncRepository: FirestoreSyncRepository? = null,
    onBack: () -> Unit
) {
    val prefs by repository.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = null)
    val userXp = prefs?.totalXp ?: 0
    val userName = prefs?.studentName ?: "You"
    val userSolved = prefs?.totalSolved ?: 0

    val scope = rememberCoroutineScope()
    var aspirants by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    val isEmailVerified = currentUser?.isEmailVerified == true && !currentUser.isAnonymous
    val hasEmail = !currentUser?.email.isNullOrBlank()

    fun refreshLeaderboard() {
        scope.launch {
            isLoading = true
            if (firestoreSyncRepository != null) {
                aspirants = firestoreSyncRepository.fetchLiveLeaderboard(userXp, userName, userSolved)
            } else {
                aspirants = emptyList()
            }
            isLoading = false
        }
    }

    LaunchedEffect(userXp, userName, userSolved) {
        refreshLeaderboard()
    }

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "🏆 CET Leaderboard",
                onBack = onBack,
                customActions = {
                    IconButton(
                        onClick = { refreshLeaderboard() },
                        modifier = Modifier.testTag("refresh_leaderboard_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Leaderboard",
                            tint = QuestAccentGold
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDone,
                        contentDescription = "Cloud Synced",
                        tint = QuestCyanAccent,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Live Verified CET Rankings",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextPrimary
                        )
                        Text(
                            "Strictly authentic: Only students registered with a verified email address appear here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuestTextSecondary
                        )
                    }
                }
            }

            if (!isEmailVerified) {
                Spacer(Modifier.height(10.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestWarningOrange.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestWarningOrange.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔒", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (!hasEmail) "You are browsing as Guest. Log in with a real email to compete on the live leaderboard!"
                            else "Verify your registered email address to appear on the official Maharashtra CET Leaderboard.",
                            style = MaterialTheme.typography.bodySmall.copy(color = QuestWarningOrange, fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = QuestAccentGold)
                }
            } else if (aspirants.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎖️", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Be the First on the Board!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextPrimary
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Log in with a verified email, earn XP by solving CET math problems, and take the #1 spot!",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuestTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(aspirants, key = { "${it.name}_${it.rank}" }) { entry ->
                        LeaderboardCard(entry = entry)
                    }
                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LeaderboardCard(entry: LeaderboardEntry) {
    val isTop3 = entry.rank in 1..3
    val rankColor = when (entry.rank) {
        1 -> QuestAccentGold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> QuestNavySurface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                if (entry.isUser) 1.5.dp else 1.dp,
                if (entry.isUser) QuestAccentGold else QuestNavyBorder,
                RoundedCornerShape(14.dp)
            )
            .testTag(if (entry.isUser) "user_leaderboard_row" else "leaderboard_row_${entry.rank}"),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isUser) QuestPrimaryBlue.copy(alpha = 0.2f) else QuestNavyCard
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(rankColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${entry.rank}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                    color = if (isTop3) Color.Black else QuestTextPrimary
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (entry.isUser) QuestAccentGoldLight else QuestTextPrimary
                )
                Text(
                    text = entry.city,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = QuestTextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.xp} XP",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestAccentGoldLight
                )
                Text(
                    text = "${entry.solved} solved",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = QuestTextTertiary
                )
            }
        }
    }
}
