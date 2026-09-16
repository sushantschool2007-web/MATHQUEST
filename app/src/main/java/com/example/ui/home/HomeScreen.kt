package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.QuestMetricCard
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onContinueLearning: (String) -> Unit,
    onQuickPractice: () -> Unit,
    onChapters: () -> Unit,
    onMockTest: () -> Unit,
    onFormulaBook: () -> Unit,
    onMistakeBook: () -> Unit,
    onDailyChallenge: () -> Unit,
    onBossBattle: () -> Unit,
    onLeaderboard: () -> Unit,
    onAchievements: () -> Unit,
    onAnalytics: () -> Unit,
    onProfile: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val prefs = state.userPrefs
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "CET MATH QUEST",
                xp = prefs?.totalXp ?: 50,
                streak = prefs?.streakDays ?: 1,
                hearts = prefs?.hearts ?: 5
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Profile & Level Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfile() }
                    .testTag("home_profile_card"),
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(QuestPrimaryBlue, QuestAccentGold)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.levelInfo.rankBadge,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = prefs?.studentName ?: "Aspirant",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = QuestTextPrimary
                                )
                                Text(
                                    text = "Level ${state.levelInfo.levelNumber} — ${state.levelInfo.title}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = QuestAccentGoldLight
                                )
                            }
                        }

                        Surface(
                            color = QuestPrimaryBlue.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "CET ${prefs?.targetYear ?: "2026"}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestPrimaryBlueLight,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // XP Progress bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Level Progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuestTextSecondary
                        )
                        Text(
                            text = "${prefs?.totalXp ?: 0} / ${state.nextLevelInfo?.minXP ?: 5000} XP",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = QuestAccentGoldLight
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { state.levelProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = QuestAccentGold,
                        trackColor = QuestNavySurface
                    )
                }
            }

            // 2. Metrics Grid: Questions Solved, Accuracy, Study Time, Streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val totalSolved = prefs?.totalSolved ?: 0
                val totalCorrect = prefs?.totalCorrect ?: 0
                val accuracy = if (totalSolved > 0) ((totalCorrect.toFloat() / totalSolved) * 100).toInt() else 0

                QuestMetricCard(
                    label = "Solved",
                    value = "$totalSolved",
                    icon = "🎯",
                    accentColor = QuestPrimaryBlue,
                    modifier = Modifier.weight(1f)
                )
                QuestMetricCard(
                    label = "Accuracy",
                    value = "$accuracy%",
                    icon = "⚡",
                    accentColor = QuestSuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                QuestMetricCard(
                    label = "Study Time",
                    value = "${prefs?.studyTimeMinutes ?: 15}m",
                    icon = "⏱️",
                    accentColor = QuestAccentAmber,
                    modifier = Modifier.weight(1f)
                )
                QuestMetricCard(
                    label = "Streak",
                    value = "${prefs?.streakDays ?: 1}d",
                    icon = "🔥",
                    accentColor = QuestErrorRed,
                    modifier = Modifier.weight(1f)
                )
            }

            // 3. Continue Learning Banner (Active Chapter)
            val currentChap = state.activeChapterProgress?.chapterName ?: (prefs?.activeChapter ?: "Trigonometry II")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onContinueLearning(currentChap) }
                    .testTag("continue_learning_banner"),
                colors = CardDefaults.cardColors(
                    containerColor = QuestNavyCard
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, QuestPrimaryBlue.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(QuestPrimaryBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📐", fontSize = 26.sp)
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CURRENT CHAPTER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = QuestPrimaryBlueLight
                        )
                        Text(
                            text = currentChap,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { (state.activeChapterProgress?.mastery ?: 30).toFloat() / 100f },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = QuestSuccessGreen,
                                trackColor = QuestNavySurface
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "${state.activeChapterProgress?.mastery ?: 30}%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestSuccessGreen
                            )
                        }
                    }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        onClick = { onContinueLearning(currentChap) },
                        colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "CONTINUE",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // 4. Primary Quest Hub (Academic + Gaming Action Buttons)
            Text(
                text = "QUEST ARENA",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                ),
                color = QuestTextTertiary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionTile(
                    title = "Quick Practice",
                    subtitle = "Adaptive Speed Drill",
                    icon = "⚡",
                    accentColor = QuestPrimaryBlue,
                    onClick = onQuickPractice,
                    modifier = Modifier.weight(1f),
                    tag = "tile_quick_practice"
                )
                ActionTile(
                    title = "Mock Test",
                    subtitle = "2026 MHT-CET Pattern",
                    icon = "📝",
                    accentColor = QuestAccentGold,
                    onClick = onMockTest,
                    modifier = Modifier.weight(1f),
                    tag = "tile_mock_test"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionTile(
                    title = "Chapters",
                    subtitle = "XI & XII Syllabus",
                    icon = "📚",
                    accentColor = QuestSuccessGreen,
                    onClick = onChapters,
                    modifier = Modifier.weight(1f),
                    tag = "tile_chapters"
                )
                ActionTile(
                    title = "Boss Battle",
                    subtitle = "Defeat Chapter Bosses",
                    icon = "👹",
                    accentColor = QuestErrorRed,
                    onClick = onBossBattle,
                    modifier = Modifier.weight(1f),
                    tag = "tile_boss_battle"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionTile(
                    title = "Daily Challenge",
                    subtitle = if (state.dailyChallengeCompleted) "Completed (+100 XP)" else "10 Qs • +100 XP",
                    icon = "🌟",
                    accentColor = QuestAccentAmber,
                    onClick = onDailyChallenge,
                    modifier = Modifier.weight(1f),
                    tag = "tile_daily_challenge"
                )
                ActionTile(
                    title = "Formula Book",
                    subtitle = "Search & Bookmarks",
                    icon = "📖",
                    accentColor = QuestPrimaryBlueLight,
                    onClick = onFormulaBook,
                    modifier = Modifier.weight(1f),
                    tag = "tile_formula_book"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionTile(
                    title = "Mistake Book",
                    subtitle = "${state.unresolvedMistakeCount} to review",
                    icon = "🛠️",
                    accentColor = QuestErrorRed,
                    onClick = onMistakeBook,
                    modifier = Modifier.weight(1f),
                    tag = "tile_mistake_book"
                )
                ActionTile(
                    title = "Leaderboard",
                    subtitle = "CET Rank Simulation",
                    icon = "🏆",
                    accentColor = QuestAccentGold,
                    onClick = onLeaderboard,
                    modifier = Modifier.weight(1f),
                    tag = "tile_leaderboard"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionTile(
                    title = "Analytics",
                    subtitle = "Strengths & Weaknesses",
                    icon = "📊",
                    accentColor = QuestSuccessGreen,
                    onClick = onAnalytics,
                    modifier = Modifier.weight(1f),
                    tag = "tile_analytics"
                )
                ActionTile(
                    title = "Achievements",
                    subtitle = "Badges & Rewards",
                    icon = "🎖️",
                    accentColor = QuestAccentGoldLight,
                    onClick = onAchievements,
                    modifier = Modifier.weight(1f),
                    tag = "tile_achievements"
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ActionTile(
    title: String,
    subtitle: String,
    icon: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, QuestNavyBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(tag),
        colors = CardDefaults.cardColors(containerColor = QuestNavyCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = icon, fontSize = 22.sp)
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = QuestTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = QuestTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
