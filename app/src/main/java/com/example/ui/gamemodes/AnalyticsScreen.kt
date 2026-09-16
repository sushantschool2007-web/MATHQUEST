package com.example.ui.gamemodes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.components.QuestMetricCard
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    repository: MathRepository,
    onBack: () -> Unit
) {
    val prefs by repository.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = null)
    val chapters by repository.allChapterProgress.collectAsStateWithLifecycle(initialValue = emptyList())
    val mockResults by repository.allMockTestResults.collectAsStateWithLifecycle(initialValue = emptyList())

    val totalSolved = prefs?.totalSolved ?: 0
    val totalCorrect = prefs?.totalCorrect ?: 0
    val overallAccuracy = if (totalSolved > 0) ((totalCorrect.toFloat() / totalSolved.toFloat()) * 100).toInt() else 0

    val strongChapters = chapters.filter { it.mastery >= 50 }
    val weakChapters = chapters.filter { it.questionsSolved > 0 && it.accuracy < 60 }

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "📊 Prep Analytics",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    "OVERVIEW PERFORMANCE",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = QuestTextTertiary
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuestMetricCard(
                        label = "Overall Accuracy",
                        value = "$overallAccuracy%",
                        icon = "🎯",
                        accentColor = QuestSuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    QuestMetricCard(
                        label = "Total Solved",
                        value = "$totalSolved",
                        icon = "⚡",
                        accentColor = QuestPrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )
                    QuestMetricCard(
                        label = "Mocks Given",
                        value = "${mockResults.size}",
                        icon = "📝",
                        accentColor = QuestAccentGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Strong vs Weak Topic Insight Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "STRENGTHS & WEAKNESSES",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextTertiary
                        )
                        Spacer(Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "💪 Strong Chapters",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = QuestSuccessGreen
                                )
                                Spacer(Modifier.height(4.dp))
                                if (strongChapters.isEmpty()) {
                                    Text("Keep practicing to build 50%+ mastery.", style = MaterialTheme.typography.bodySmall, color = QuestTextSecondary)
                                } else {
                                    strongChapters.take(3).forEach {
                                        Text("• ${it.chapterName} (${it.mastery}%)", style = MaterialTheme.typography.bodySmall, color = QuestTextPrimary)
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "⚠️ Focus Needed",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = QuestErrorRed
                                )
                                Spacer(Modifier.height(4.dp))
                                if (weakChapters.isEmpty()) {
                                    Text("No critical weak chapters detected yet!", style = MaterialTheme.typography.bodySmall, color = QuestTextSecondary)
                                } else {
                                    weakChapters.take(3).forEach {
                                        Text("• ${it.chapterName} (${it.accuracy.toInt()}%)", style = MaterialTheme.typography.bodySmall, color = QuestTextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Chapter Mastery Breakdown List
            item {
                Text(
                    "CHAPTER MASTERY BREAKDOWN",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = QuestTextTertiary
                )
            }

            val topChapters = chapters.take(12)
            items(topChapters.size) { index ->
                val ch = topChapters[index]
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = ch.chapterName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = QuestTextPrimary
                            )
                            Text(
                                text = "${ch.mastery}%",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (ch.mastery >= 70) QuestSuccessGreen else QuestAccentGoldLight
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { ch.mastery.toFloat() / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(CircleShape),
                            color = if (ch.mastery >= 70) QuestSuccessGreen else QuestPrimaryBlue,
                            trackColor = QuestNavyDark
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
