package com.example.ui.gamemodes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.repository.MathRepository
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

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
    onBack: () -> Unit
) {
    val prefs by repository.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = null)
    val userXp = prefs?.totalXp ?: 150
    val userName = prefs?.studentName ?: "You"
    val userSolved = prefs?.totalSolved ?: 10

    val aspirants = remember(userXp) {
        val staticList = listOf(
            LeaderboardEntry(1, "Tanvi Deshmukh", "Pune (COEP Aspirant)", 4850, 142),
            LeaderboardEntry(2, "Rohan Kulkarni", "Mumbai (VJTI Aspirant)", 4120, 128),
            LeaderboardEntry(3, "Pranav Shinde", "Nagpur (VNIT Aspirant)", 3650, 110),
            LeaderboardEntry(4, "Aditi Joshi", "Nashik (PICT Aspirant)", 2980, 95),
            LeaderboardEntry(5, "Atharva Patil", "Kolhapur (Walchand Aspirant)", 2410, 84),
            LeaderboardEntry(6, "Neha Sawant", "Thane (SPIT Aspirant)", 1890, 68),
            LeaderboardEntry(7, "Siddharth More", "Aurangabad (VIT Aspirant)", 1250, 48),
            LeaderboardEntry(8, "Vaishnavi Pawar", "Solapur (PCCOE Aspirant)", 820, 34),
            LeaderboardEntry(9, "Omkar Gaikwad", "Satara", 510, 22)
        )

        // Insert user dynamically based on XP
        val combined = staticList.toMutableList()
        val userEntry = LeaderboardEntry(0, "$userName (You)", "Maharashtra Aspirant", userXp, userSolved, isUser = true)
        combined.add(userEntry)
        combined.sortedByDescending { it.xp }.mapIndexed { idx, item ->
            item.copy(rank = idx + 1)
        }
    }

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "🏆 CET Leaderboard",
                onBack = onBack
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
                    Text("⚡", fontSize = 24.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            "Simulated Maharashtra CET Rank",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextPrimary
                        )
                        Text(
                            "Ranks update in real-time as you solve questions, finish mock tests, and earn XP.",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuestTextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(aspirants, key = { it.name }) { entry ->
                    LeaderboardCard(entry = entry)
                }
                item {
                    Spacer(Modifier.height(24.dp))
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
