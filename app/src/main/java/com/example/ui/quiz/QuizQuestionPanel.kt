package com.example.ui.quiz

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.QuestionEntity
import com.example.ui.components.formatLatexToReadableMath
import com.example.ui.theme.*

/**
 * Quiz & Practice Component strictly adhering to the Design System Specifications:
 * - Top navigation showing progress bar, remaining time clock, and current question number.
 * - Clean question card with distinct choice selection states:
 *     * Light Mode: Border outline with subtle blue tint on selection (#2563EB border, subtle blue container).
 *     * Dark Mode: Glowing border with dark indigo background fill on selection (#6366F1 border, electric indigo alpha fill).
 * - Step-by-step collapsible solution panel appearing after submission.
 */
@Composable
fun QuizQuestionPanel(
    question: QuestionEntity,
    currentIndex: Int,
    totalQuestions: Int,
    remainingSeconds: Int?,
    selectedOption: String?,
    isSubmitted: Boolean,
    isCorrect: Boolean,
    onOptionSelected: (String) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = ThemeController.isDarkTheme
) {
    var isSolutionExpanded by remember(question.id) { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Top Navigation: Progress bar, remaining time clock, and current question number
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentIndex + 1} of $totalQuestions",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                )

                if (remainingSeconds != null) {
                    val minutes = remainingSeconds / 60
                    val seconds = remainingSeconds % 60
                    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
                    val isUrgent = remainingSeconds < 60

                    Surface(
                        color = (if (isUrgent) Color(0xFFDC2626) else (if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary)).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            (if (isUrgent) Color(0xFFDC2626) else (if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary)).copy(alpha = 0.4f)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = if (isUrgent) Color(0xFFDC2626) else (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = timeFormatted,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isUrgent) Color(0xFFDC2626) else (if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary)
                            )
                        }
                    }
                }
            }

            // Progress bar
            val progressFraction = ((currentIndex + 1).toFloat() / totalQuestions.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary,
                trackColor = if (isDark) DarkSpectrumBorder else LightSpectrumBorder
            )
        }

        // 2. Clean Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(
                    1.dp,
                    if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                    RoundedCornerShape(18.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
            )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                if (question.isPyq) {
                    Surface(
                        color = (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Text(
                            text = "★ MHT-CET PYQ ${question.pyqYear} • +2 Marks",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = formatLatexToReadableMath(question.question),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 25.sp
                    ),
                    color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                )
            }
        }

        // 3. Options with Strict Light/Dark Mode Selection States
        val options = listOf(
            "A" to question.optionA,
            "B" to question.optionB,
            "C" to question.optionC,
            "D" to question.optionD
        )

        options.forEach { (label, optionText) ->
            val isSelected = selectedOption == label
            val isAnswerCorrect = question.correctAnswer == label

            // Light Mode: Border outline with subtle blue tint on selection
            // Dark Mode: Glowing border with dark indigo background fill on selection
            val containerColor = when {
                isSubmitted && isAnswerCorrect -> (if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess).copy(alpha = 0.2f)
                isSubmitted && isSelected && !isCorrect -> (if (isDark) DarkSpectrumError else LightSpectrumError).copy(alpha = 0.2f)
                isSelected -> {
                    if (isDark) DarkSpectrumActionElectric.copy(alpha = 0.28f) // glowing border with dark indigo fill
                    else Color(0xFFEFF6FF) // subtle blue tint in light mode
                }
                else -> if (isDark) DarkSpectrumCard else LightSpectrumCard
            }

            val borderColor = when {
                isSubmitted && isAnswerCorrect -> if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess
                isSubmitted && isSelected && !isCorrect -> if (isDark) DarkSpectrumError else LightSpectrumError
                isSelected -> {
                    if (isDark) DarkSpectrumActionElectric // glowing border in dark mode
                    else LightSpectrumActionPrimary // #2563EB royal blue border outline in light mode
                }
                else -> if (isDark) DarkSpectrumBorder else LightSpectrumBorder
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = containerColor),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isSubmitted) {
                        onOptionSelected(label)
                    }
                    .testTag("option_$label")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = when {
                            isSubmitted && isAnswerCorrect -> if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess
                            isSubmitted && isSelected && !isCorrect -> if (isDark) DarkSpectrumError else LightSpectrumError
                            isSelected -> if (isDark) DarkSpectrumActionElectric else LightSpectrumActionPrimary
                            else -> if (isDark) DarkSpectrumBackground else Color(0xFFE2E8F0)
                        },
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected || (isSubmitted && (isAnswerCorrect || isSelected))) Color.White
                                else (if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary)
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = formatLatexToReadableMath(optionText),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal),
                        color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Submit / Next Actions
        if (!isSubmitted) {
            Button(
                onClick = onSubmit,
                enabled = selectedOption != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary,
                    disabledContainerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_answer_button")
            ) {
                Text(
                    text = "Submit Answer",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (selectedOption != null) Color.White else (if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary)
                )
            }
        } else {
            // 4. Step-by-Step Collapsible Solution Panel appearing after submission
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        if (isCorrect) (if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess).copy(alpha = 0.5f)
                        else (if (isDark) DarkSpectrumError else LightSpectrumError).copy(alpha = 0.5f),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect)
                        (if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess).copy(alpha = 0.12f)
                    else
                        (if (isDark) DarkSpectrumError else LightSpectrumError).copy(alpha = 0.12f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header of solution panel
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSolutionExpanded = !isSolutionExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isCorrect) "✓ Correct Answer" else "✗ Incorrect (Correct: ${question.correctAnswer})",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isCorrect) (if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess) else (if (isDark) DarkSpectrumError else LightSpectrumError)
                            )
                        }

                        Icon(
                            imageVector = if (isSolutionExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isSolutionExpanded) "Collapse" else "Expand",
                            tint = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                        )
                    }

                    AnimatedVisibility(
                        visible = isSolutionExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "STEP-BY-STEP SOLUTION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                            )

                            Surface(
                                color = if (isDark) DarkSpectrumSurface else LightSpectrumSurface,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = formatLatexToReadableMath(question.explanation),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        lineHeight = 22.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            if (question.shortcut.isNotBlank()) {
                                Surface(
                                    color = (if (isDark) DarkSpectrumAmber else Color(0xFFEA580C)).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        (if (isDark) DarkSpectrumAmber else Color(0xFFEA580C)).copy(alpha = 0.35f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "⚡ CET Speed Shortcut: ${question.shortcut}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = if (isDark) DarkSpectrumAmber else Color(0xFFC2410C),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("next_question_button")
            ) {
                Text(
                    text = if (currentIndex < totalQuestions - 1) "Next Question →" else "Complete Session",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}
