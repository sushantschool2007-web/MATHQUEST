package com.example.ui.mocktest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.QuestMetricCard
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

@Composable
fun MockTestResultScreen(
    score: Int,
    percentage: Float,
    accuracy: Float,
    correct: Int,
    incorrect: Int,
    unattempted: Int,
    timeTaken: Int,
    xiScore: Int,
    xiiScore: Int,
    onHome: () -> Unit
) {
    val scrollState = rememberScrollState()

    val estimatedPercentile = when {
        score >= 90 -> "99.5 - 99.9 %ile"
        score >= 80 -> "98.0 - 99.4 %ile"
        score >= 70 -> "95.0 - 97.9 %ile"
        score >= 55 -> "90.0 - 94.9 %ile"
        score >= 40 -> "80.0 - 89.9 %ile"
        else -> "Below 80 %ile"
    }

    val collegeProjection = when {
        score >= 85 -> "COEP Pune / VJTI Mumbai (Computer Engineering)"
        score >= 75 -> "SPIT Mumbai / PICT Pune (IT / AI-DS)"
        score >= 60 -> "Walchand Sangli / VIT Pune (Core / Tech)"
        else -> "Keep practicing to push past 75+ for premier institutes!"
    }

    val minutes = timeTaken / 60
    val seconds = timeTaken % 60

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "Mock Test Analysis",
                onBack = onHome
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
            // Big Score Header
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, QuestAccentGold.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOTAL SCORE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = QuestTextTertiary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$score / 100",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 42.sp
                        ),
                        color = QuestAccentGold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${percentage.toInt()}% Marks • Accuracy: ${accuracy.toInt()}%",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = QuestTextSecondary
                    )

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = QuestNavyBorder)
                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "Projected MHT-CET Percentile",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuestTextTertiary
                    )
                    Text(
                        text = estimatedPercentile,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestSuccessGreen
                    )
                }
            }

            // Target College Bracket Insight
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavySurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🏛️", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Target College Projection",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestPrimaryBlueLight
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        collegeProjection,
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextPrimary
                    )
                }
            }

            // Quick Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuestMetricCard(
                    label = "Correct",
                    value = "$correct",
                    icon = "✅",
                    accentColor = QuestSuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                QuestMetricCard(
                    label = "Incorrect",
                    value = "$incorrect",
                    icon = "❌",
                    accentColor = QuestErrorRed,
                    modifier = Modifier.weight(1f)
                )
                QuestMetricCard(
                    label = "Skipped",
                    value = "$unattempted",
                    icon = "⏸️",
                    accentColor = QuestTextTertiary,
                    modifier = Modifier.weight(1f)
                )
                QuestMetricCard(
                    label = "Time",
                    value = "${minutes}m",
                    icon = "⏱️",
                    accentColor = QuestAccentAmber,
                    modifier = Modifier.weight(1f)
                )
            }

            // Syllabus Breakdown: Class XI vs Class XII
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "SYLLABUS SPLIT BREAKDOWN",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextTertiary
                    )
                    Spacer(Modifier.height(12.dp))

                    // Class XII
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Std. XII (80% Weight)", style = MaterialTheme.typography.bodyMedium, color = QuestTextPrimary)
                        Text("$xiiScore / 80 Marks", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = QuestAccentGoldLight)
                    }
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (xiiScore.toFloat() / 80f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = QuestAccentGold,
                        trackColor = QuestNavySurface
                    )

                    Spacer(Modifier.height(14.dp))

                    // Class XI
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Std. XI (20% Weight)", style = MaterialTheme.typography.bodyMedium, color = QuestTextPrimary)
                        Text("$xiScore / 20 Marks", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = QuestPrimaryBlueLight)
                    }
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (xiScore.toFloat() / 20f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = QuestPrimaryBlue,
                        trackColor = QuestNavySurface
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = onHome,
                colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("return_to_quest_hub")
            ) {
                Text(
                    "Back to Quest Hub",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
