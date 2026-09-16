package com.example.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.OptionCard
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onComplete: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = QuestNavyDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.step) {
                1 -> OnboardingInfoStep(
                    state = state,
                    onNameChange = viewModel::updateName,
                    onYearChange = viewModel::updateTargetYear,
                    onLevelChange = viewModel::updatePrepLevel,
                    onGoalChange = viewModel::updateDailyGoal,
                    onTargetScoreChange = viewModel::updateTargetScore,
                    onProceed = viewModel::proceedToDiagnostic
                )
                2 -> OnboardingDiagnosticStep(
                    state = state,
                    questions = viewModel.diagnosticQuestions,
                    onOptionSelect = viewModel::selectDiagnosticOption,
                    onSubmitAnswer = viewModel::submitDiagnosticAnswer
                )
                3 -> OnboardingResultStep(
                    state = state,
                    onFinish = { viewModel.completeOnboarding(onComplete) }
                )
            }
        }
    }
}

@Composable
private fun OnboardingInfoStep(
    state: OnboardingUiState,
    onNameChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onLevelChange: (String) -> Unit,
    onGoalChange: (Int) -> Unit,
    onTargetScoreChange: (Int) -> Unit,
    onProceed: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "⚡ CET MATH QUEST",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            ),
            color = QuestAccentGold
        )
        Text(
            text = "Master MHT-CET Mathematics Through Learning + Gaming",
            style = MaterialTheme.typography.bodySmall,
            color = QuestTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(28.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Welcome, Future Engineer!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
                Text(
                    text = "Let's personalize your MHT-CET PCM Mathematics preparation roadmap.",
                    style = MaterialTheme.typography.bodySmall,
                    color = QuestTextSecondary
                )

                Spacer(Modifier.height(20.dp))

                // Name Input
                Text(
                    text = "Your Name",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = QuestTextPrimary
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = state.studentName,
                    onValueChange = onNameChange,
                    placeholder = { Text("e.g. Aditya Patil", color = QuestTextTertiary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = QuestPrimaryBlue,
                        unfocusedBorderColor = QuestNavyBorder,
                        focusedTextColor = QuestTextPrimary,
                        unfocusedTextColor = QuestTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("onboarding_name_input")
                )

                Spacer(Modifier.height(16.dp))

                // Target Year
                Text(
                    text = "Target MHT-CET Year",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = QuestTextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("2026", "2027").forEach { year ->
                        val isSelected = state.targetYear == year
                        FilterChip(
                            selected = isSelected,
                            onClick = { onYearChange(year) },
                            label = { Text("CET $year") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuestPrimaryBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Daily Study Time
                Text(
                    text = "Preferred Daily Practice Time",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = QuestTextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(15, 30, 45, 60).forEach { mins ->
                        val isSelected = state.dailyGoalMinutes == mins
                        FilterChip(
                            selected = isSelected,
                            onClick = { onGoalChange(mins) },
                            label = { Text("${mins}m") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuestAccentGold,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Target Score
                Text(
                    text = "Target Maths Score: ${state.targetScore}/100",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = QuestAccentGoldLight
                )
                Slider(
                    value = state.targetScore.toFloat(),
                    onValueChange = { onTargetScoreChange(it.toInt()) },
                    valueRange = 50f..100f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = QuestAccentGold,
                        activeTrackColor = QuestAccentGold,
                        inactiveTrackColor = QuestNavyBorder
                    )
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onProceed,
            colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_proceed_button")
        ) {
            Text(
                text = "Take 2-Min Diagnostic Test →",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun OnboardingDiagnosticStep(
    state: OnboardingUiState,
    questions: List<DiagnosticQuestion>,
    onOptionSelect: (String) -> Unit,
    onSubmitAnswer: () -> Unit
) {
    val q = questions[state.diagnosticIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Diagnostic Test",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestAccentGold
                )
                Text(
                    text = "Question ${state.diagnosticIndex + 1} of ${questions.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = QuestTextSecondary
                )
            }

            Spacer(Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { (state.diagnosticIndex + 1).toFloat() / questions.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = QuestAccentGold,
                trackColor = QuestNavyBorder,
            )

            Spacer(Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = q.topic,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestPrimaryBlueLight
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp
                        ),
                        color = QuestTextPrimary
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    "A" to q.optionA,
                    "B" to q.optionB,
                    "C" to q.optionC,
                    "D" to q.optionD
                ).forEach { (letter, text) ->
                    OptionCard(
                        optionLetter = letter,
                        optionText = text,
                        isSelected = state.selectedOption == letter,
                        isAnswerRevealed = false,
                        isCorrectOption = false,
                        onClick = { onOptionSelect(letter) }
                    )
                }
            }
        }

        Button(
            onClick = onSubmitAnswer,
            enabled = state.selectedOption != null,
            colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_diagnostic_answer")
        ) {
            Text(
                text = if (state.diagnosticIndex < questions.size - 1) "Next Question →" else "View My Result →",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun OnboardingResultStep(
    state: OnboardingUiState,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎯", fontSize = 48.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Diagnostic Complete!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
            color = QuestTextPrimary
        )

        Spacer(Modifier.height(24.dp))

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
                Text(
                    text = "YOUR CURRENT MATHS LEVEL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    ),
                    color = QuestTextTertiary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.calculatedMathLevel.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = QuestAccentGold
                )
                Text(
                    text = state.calculatedMathLevel.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = QuestTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = QuestNavyBorder)
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Recommended Starting Chapter:",
                    style = MaterialTheme.typography.labelMedium,
                    color = QuestTextSecondary
                )
                Spacer(Modifier.height(4.dp))
                Surface(
                    color = QuestPrimaryBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue)
                ) {
                    Text(
                        text = "📖 ${state.recommendedChapter}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestPrimaryBlueLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onFinish,
            colors = ButtonDefaults.buttonColors(containerColor = QuestAccentGold),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("start_quest_button")
        ) {
            Text(
                text = "START MY QUEST ⚔️",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            )
        }
    }
}
