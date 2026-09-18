package com.example.ui.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class SubjectTrack(
    val title: String,
    val subtitle: String,
    val icon: String,
    val progress: Float, // 0f to 1f
    val chapterSummary: String,
    val accentColor: Color,
    val routeKeyword: String
)

@Composable
fun SubjectSelectionSection(
    onTrackSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = ThemeController.isDarkTheme

    val tracks = listOf(
        SubjectTrack(
            title = "Class 11 Mathematics",
            subtitle = "Foundations & CET 20% Weightage",
            icon = "📘",
            progress = 0.45f,
            chapterSummary = "8 Chapters • Trigonometry, Circles, Conics",
            accentColor = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
            routeKeyword = "Std 11"
        ),
        SubjectTrack(
            title = "Class 12 Mathematics",
            subtitle = "Core Syllabus & CET 80% Weightage",
            icon = "📗",
            progress = 0.68f,
            chapterSummary = "15 Chapters • Calculus, Vectors, 3D Geometry",
            accentColor = if (isDark) DarkSpectrumActionElectric else LightSpectrumActionIndigo,
            routeKeyword = "Std 12"
        ),
        SubjectTrack(
            title = "MHT-CET Math Target",
            subtitle = "Full Exam Simulation & 100 Marks Target",
            icon = "🎯",
            progress = 0.54f,
            chapterSummary = "50 Questions • 90 Mins • Speed & Accuracy",
            accentColor = if (isDark) Color(0xFF10B981) else Color(0xFF16A34A),
            routeKeyword = "MHT-CET"
        ),
        SubjectTrack(
            title = "JEE Main Advanced Prep",
            subtitle = "Higher Difficulty & Deep Multi-concept Problems",
            icon = "🚀",
            progress = 0.32f,
            chapterSummary = "Advanced Analytical & Multi-Correct Practice",
            accentColor = if (isDark) Color(0xFFF59E0B) else Color(0xFFD97706),
            routeKeyword = "JEE Main"
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SUBJECT & EXAM TRACKS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
            )
            Text(
                text = "Std 11, 12, CET, JEE",
                style = MaterialTheme.typography.labelSmall,
                color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
            )
        }

        tracks.forEach { track ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onTrackSelected(track.routeKeyword) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    1.dp,
                                    track.accentColor.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(track.icon, fontSize = 20.sp)
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = track.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                            )
                            Text(
                                text = track.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                            )
                        }

                        Text(
                            text = "${(track.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = track.accentColor
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { track.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = track.accentColor,
                        trackColor = if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    )

                    Spacer(Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = track.chapterSummary,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Explore →",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = track.accentColor,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
