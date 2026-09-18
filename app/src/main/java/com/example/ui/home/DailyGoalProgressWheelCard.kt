package com.example.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DailyGoalProgressWheelCard(
    completedSubtopics: Int,
    targetSubtopics: Int = 5,
    streakDays: Int,
    levelNumber: Int,
    levelTitle: String,
    currentXp: Int,
    nextLevelXp: Int,
    modifier: Modifier = Modifier
) {
    val progress = (completedSubtopics.toFloat() / targetSubtopics.toFloat()).coerceIn(0f, 1f)
    val isDark = ThemeController.isDarkTheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "DAILY GOAL & XP PROGRESS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Progress Wheel
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(86.dp)
                ) {
                    val trackColor = if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    val activeColor = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 8.dp.toPx()
                        // Track
                        drawArc(
                            color = trackColor,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Progress sweep
                        drawArc(
                            color = activeColor,
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$completedSubtopics/$targetSubtopics",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                        )
                        Text(
                            text = "subtopics",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary,
                            fontSize = 9.sp
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Level $levelNumber • $levelTitle",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                        )

                        // Streak Counter badge
                        Surface(
                            color = (if (isDark) DarkSpectrumAmber else Color(0xFFEA580C)).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                (if (isDark) DarkSpectrumAmber else Color(0xFFEA580C)).copy(alpha = 0.4f)
                            )
                        ) {
                            Text(
                                text = "🔥 $streakDays Day Streak",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumAmber else Color(0xFFEA580C),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // XP Progress bar
                    val xpProgress = (currentXp.toFloat() / nextLevelXp.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { xpProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionIndigo,
                        trackColor = if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$currentXp XP Earned",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                        )
                        Text(
                            text = "Next: $nextLevelXp XP",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                        )
                    }
                }
            }
        }
    }
}
