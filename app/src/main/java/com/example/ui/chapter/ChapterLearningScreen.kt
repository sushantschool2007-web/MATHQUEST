package com.example.ui.chapter

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ChapterLearningScreen(
    viewModel: ChapterLearningViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = state.chapterName,
                onBack = onBack,
                streak = if (state.comboStreak > 1) state.comboStreak else null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Tab switch: Theory/Formulas vs Practice Quest
            TabRow(
                selectedTabIndex = state.activeTab,
                containerColor = QuestNavyCard,
                contentColor = QuestAccentGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[state.activeTab]),
                        color = QuestAccentGold
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, QuestNavyBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = state.activeTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    text = { Text("Formulas & Concepts", fontWeight = if (state.activeTab == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = state.activeTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    text = { Text("Quest Arena", fontWeight = if (state.activeTab == 1) FontWeight.Bold else FontWeight.Normal) }
                )
            }

            if (state.activeTab == 0) {
                // Formulas & Concepts view
                TheorySection(state = state)
            } else {
                // Practice Quest view
                if (state.questions.isNotEmpty()) {
                    PracticeSection(
                        state = state,
                        onOptionSelect = viewModel::selectOption,
                        onSubmit = viewModel::submitAnswer,
                        onNext = viewModel::nextQuestion,
                        onHintClick = viewModel::showHint,
                        onTeachMeClick = viewModel::showTeachMe
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = QuestPrimaryBlue)
                    }
                }
            }
        }
    }

    // Hint Dialog
    if (state.showHintDialog) {
        val q = state.questions.getOrNull(state.currentQuestionIndex)
        if (q != null) {
            HintDialog(
                hints = listOf(q.hint1, q.hint2, q.hint3).filter { it.isNotBlank() },
                unlockedHintCount = state.unlockedHints,
                onUnlockNextHint = viewModel::unlockNextHint,
                onDismiss = viewModel::dismissHint
            )
        }
    }

    // Teach Me Dialog
    if (state.showTeachMeDialog) {
        val q = state.questions.getOrNull(state.currentQuestionIndex)
        if (q != null) {
            TeachMeDialog(
                given = q.teachMeGiven,
                weNeed = q.teachMeWeNeed,
                formula = q.teachMeFormula,
                substitution = q.teachMeSubstitution,
                calculation = q.teachMeCalculation,
                finalAnswer = q.teachMeFinalAnswer,
                shortcut = q.teachMeShortcut,
                onDismiss = viewModel::dismissTeachMe
            )
        }
    }
}

@Composable
private fun TheorySection(state: ChapterLearningUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "ESSENTIAL FORMULAS & RESULTS",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            color = QuestTextTertiary
        )

        if (state.formulas.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Core identities for ${state.chapterName}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Focus on high-yield formulas and standard transformation results tested in recent MHT-CET papers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestTextSecondary
                    )
                }
            }
        } else {
            state.formulas.forEach { formula ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = formula.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestAccentGold
                        )
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            color = QuestNavyDark,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = formula.formula,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp
                                ),
                                color = QuestTextPrimary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "When to Use: ${formula.whenToUse}",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuestTextSecondary
                        )
                        if (formula.shortcut.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "⚡ Shortcut: ${formula.shortcut}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = QuestAccentGoldLight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeSection(
    state: ChapterLearningUiState,
    onOptionSelect: (String) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onHintClick: () -> Unit,
    onTeachMeClick: () -> Unit
) {
    val q = state.questions[state.currentQuestionIndex]

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Progress & Level Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = QuestPrimaryBlue.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.4f))
            ) {
                Text(
                    text = q.difficulty,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = QuestPrimaryBlueLight,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Text(
                text = "Q ${state.currentQuestionIndex + 1} of ${state.questions.size}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = QuestTextSecondary
            )
        }

        LinearProgressIndicator(
            progress = { (state.currentQuestionIndex + 1).toFloat() / state.questions.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = QuestAccentGold,
            trackColor = QuestNavyBorder
        )

        // Question Card
        Card(
            colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = q.topic,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestAccentGold
                    )
                    if (q.isPyq) {
                        Surface(
                            color = QuestAccentAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = q.pyqYear ?: "PYQ",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestAccentAmber,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = q.question,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    ),
                    color = QuestTextPrimary
                )
            }
        }

        // Options List
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
                    isAnswerRevealed = state.isAnswerSubmitted,
                    isCorrectOption = letter == q.correctAnswer,
                    onClick = { onOptionSelect(letter) }
                )
            }
        }

        // Action Buttons: Hint, Teach Me, Submit / Next
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onHintClick,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestAccentGold.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuestAccentGoldLight),
                modifier = Modifier
                    .weight(1f)
                    .testTag("hint_button")
            ) {
                Text("💡 Hint (${state.unlockedHints}/3)")
            }

            OutlinedButton(
                onClick = onTeachMeClick,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuestPrimaryBlueLight),
                modifier = Modifier
                    .weight(1f)
                    .testTag("teach_me_button")
            ) {
                Text("🎓 Teach Me")
            }
        }

        // Explanation & Shortcut after submit
        AnimatedVisibility(visible = state.isAnswerSubmitted) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (state.isCorrect) QuestSuccessGreen.copy(alpha = 0.15f) else QuestErrorRed.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (state.isCorrect) QuestSuccessGreen else QuestErrorRed
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (state.isCorrect) "🎉 Correct! +${state.earnedXp} XP" else "❌ Incorrect. Correct Option: ${q.correctAnswer}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (state.isCorrect) QuestSuccessGreen else QuestErrorRed
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = q.explanation,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        color = QuestTextPrimary
                    )

                    if (q.shortcut.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))
                        Surface(
                            color = QuestAccentGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, QuestAccentGold.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚡ CET Shortcut: ${q.shortcut}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = QuestAccentGoldLight,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Submit or Next Button
        if (!state.isAnswerSubmitted) {
            Button(
                onClick = onSubmit,
                enabled = state.selectedOption != null,
                colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_question_answer")
            ) {
                Text(
                    "Submit Answer",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        } else {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = QuestAccentGold),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("next_question_button")
            ) {
                Text(
                    if (state.currentQuestionIndex < state.questions.size - 1) "Next Question →" else "Complete Chapter Session 🏆",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}
