package com.example.ui.mocktest

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.OptionCard
import com.example.ui.theme.*
import java.util.Locale

// Custom CBT Purple Colors for Marked for Review
private val ColorMarkedPurple = Color(0xFF8E24AA)
private val ColorAnsweredAndMarked = Color(0xFF6A1B9A)
private val ColorNotVisited = Color(0xFF1E293B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestScreen(
    viewModel: MockTestViewModel,
    onFinishTest: (score: Int, percentage: Float, accuracy: Float, correct: Int, incorrect: Int, unattempted: Int, timeTaken: Int, xiScore: Int, xiiScore: Int) -> Unit,
    onExit: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Handle Back Press safely during ongoing test
    BackHandler(enabled = !state.isSubmitted) {
        viewModel.showExitConfirmation()
    }

    // Automatically navigate when test submission completes (either timer or user confirm)
    LaunchedEffect(state.testResult) {
        val result = state.testResult
        if (result != null) {
            onFinishTest(
                result.score,
                result.percentage,
                result.accuracy,
                result.correctCount,
                result.incorrectCount,
                result.unattemptedCount,
                result.timeTakenSeconds,
                result.classXiScore,
                result.classXiiScore
            )
        }
    }

    // Format remaining time HH:MM:SS
    val hours = state.remainingSeconds / 3600
    val minutes = (state.remainingSeconds % 3600) / 60
    val seconds = state.remainingSeconds % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    val isLowTime = state.remainingSeconds < 5 * 60

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = viewModel::showExitConfirmation,
                        modifier = Modifier.testTag("mock_test_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit Test",
                            tint = QuestTextPrimary
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            "MHT-CET MOCK TEST",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextPrimary
                        )
                        Text(
                            "50 Questions • 100 Marks • 90 Mins",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuestTextSecondary
                        )
                    }
                },
                actions = {
                    // Timer Chip
                    Surface(
                        color = if (isLowTime) QuestErrorRed.copy(alpha = 0.2f) else QuestNavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isLowTime) QuestErrorRed else QuestAccentGold
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(if (isLowTime) "⚠️ " else "⏱️ ", fontSize = 12.sp)
                            Text(
                                text = timeFormatted,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = if (isLowTime) QuestErrorRed else QuestAccentGoldLight
                            )
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    IconButton(
                        onClick = { viewModel.togglePalette(true) },
                        modifier = Modifier.testTag("palette_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = "Question Palette",
                            tint = QuestAccentGoldLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = QuestNavyDark)
            )
        },
        bottomBar = {
            Surface(
                color = QuestNavyCard,
                tonalElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder)
            ) {
                val currentItem = state.questions.getOrNull(state.currentIndex)
                val isMarked = currentItem?.status == QuestionStatus.MARKED_FOR_REVIEW ||
                        currentItem?.status == QuestionStatus.ANSWERED_AND_MARKED

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Clear Response Button
                    OutlinedButton(
                        onClick = viewModel::clearResponse,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mock_clear_response")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = QuestTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Clear", color = QuestTextSecondary, fontSize = 11.sp)
                    }

                    // Mark / Unmark Review Button
                    OutlinedButton(
                        onClick = {
                            if (isMarked) {
                                viewModel.toggleMarkForReview()
                            } else {
                                viewModel.markForReview()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isMarked) QuestAccentAmber else ColorMarkedPurple
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isMarked) ColorMarkedPurple.copy(alpha = 0.15f) else Color.Transparent
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("mock_mark_review")
                    ) {
                        Icon(
                            imageVector = if (isMarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isMarked) QuestAccentAmber else ColorMarkedPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (isMarked) "Unmark" else "Mark Review",
                            color = if (isMarked) QuestAccentAmber else Color(0xFFBA68C8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Save & Next Button
                    Button(
                        onClick = viewModel::saveAndNext,
                        colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("mock_save_next")
                    ) {
                        Text(
                            "Save & Next",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    // Submit Test Button
                    Button(
                        onClick = viewModel::showSubmitConfirmation,
                        colors = ButtonDefaults.buttonColors(containerColor = QuestSuccessGreen),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("mock_submit_test")
                    ) {
                        Text(
                            "Submit",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (state.questions.isNotEmpty()) {
            val currentItem = state.questions[state.currentIndex]
            val q = currentItem.question

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Question Header with Prev / Next Navigation Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Question ${state.currentIndex + 1} of ${state.questions.size}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = QuestAccentGold
                            )
                            Spacer(Modifier.width(8.dp))
                            // Status indicator badge
                            StatusBadge(status = currentItem.status)
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = QuestPrimaryBlue.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Std. ${q.stdClass} (${if (q.stdClass == 11) "20% Weight • 2 Marks" else "80% Weight • 2 Marks"})",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = QuestPrimaryBlueLight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    // Prev / Next arrow buttons for smooth question browsing
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = viewModel::previousQuestion,
                            enabled = state.currentIndex > 0,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous Question",
                                tint = if (state.currentIndex > 0) QuestTextPrimary else QuestTextTertiary
                            )
                        }
                        IconButton(
                            onClick = viewModel::saveAndNext,
                            enabled = state.currentIndex < state.questions.size - 1,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Question",
                                tint = if (state.currentIndex < state.questions.size - 1) QuestTextPrimary else QuestTextTertiary
                            )
                        }
                    }
                }

                // Question Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${q.chapter} • ${q.topic}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestTextSecondary
                            )
                            Text(
                                text = "+2, 0",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestSuccessGreen
                            )
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

                // Options
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
                            isSelected = currentItem.selectedOption == letter,
                            isAnswerRevealed = false,
                            isCorrectOption = false,
                            onClick = { viewModel.selectOption(letter) }
                        )
                    }
                }

                Spacer(Modifier.height(40.dp))
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = QuestAccentGold)
            }
        }
    }

    // Question Palette BottomSheet / Dialog
    if (state.showPalette) {
        val answeredCount = state.questions.count { it.status == QuestionStatus.ANSWERED }
        val notAnsweredCount = state.questions.count { it.status == QuestionStatus.NOT_ANSWERED }
        val markedReviewCount = state.questions.count { it.status == QuestionStatus.MARKED_FOR_REVIEW }
        val ansAndMarkedCount = state.questions.count { it.status == QuestionStatus.ANSWERED_AND_MARKED }
        val notVisitedCount = state.questions.count { it.status == QuestionStatus.NOT_VISITED }

        AlertDialog(
            onDismissRequest = { viewModel.togglePalette(false) },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Question Palette (50 Qs)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                    Text(
                        "${answeredCount + ansAndMarkedCount}/50 Solved",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestSuccessGreen
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Full MHT-CET 5-Category Legend with counts
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(QuestNavyCard, RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PaletteLegendItem("Answered ($answeredCount)", QuestSuccessGreen)
                            PaletteLegendItem("Not Answered ($notAnsweredCount)", QuestErrorRed)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PaletteLegendItem("Marked for Review ($markedReviewCount)", ColorMarkedPurple)
                            PaletteLegendItem("Ans & Marked ($ansAndMarkedCount)", ColorAnsweredAndMarked, hasCheck = true)
                        }
                        PaletteLegendItem("Not Visited ($notVisitedCount)", ColorNotVisited)
                    }

                    Spacer(Modifier.height(14.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(280.dp)
                    ) {
                        itemsIndexed(state.questions) { index, item ->
                            val isCurrent = index == state.currentIndex
                            val (bgColor, textColor, hasCheck) = when (item.status) {
                                QuestionStatus.ANSWERED -> Triple(QuestSuccessGreen, Color.White, false)
                                QuestionStatus.NOT_ANSWERED -> Triple(QuestErrorRed, Color.White, false)
                                QuestionStatus.MARKED_FOR_REVIEW -> Triple(ColorMarkedPurple, Color.White, false)
                                QuestionStatus.ANSWERED_AND_MARKED -> Triple(ColorAnsweredAndMarked, Color.White, true)
                                QuestionStatus.NOT_VISITED -> Triple(ColorNotVisited, QuestTextTertiary, false)
                            }

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bgColor)
                                    .border(
                                        width = if (isCurrent) 2.dp else 1.dp,
                                        color = if (isCurrent) QuestAccentGold else QuestNavyBorder,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.navigateToQuestion(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = textColor
                                )

                                if (hasCheck) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(3.dp)
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(QuestSuccessGreen)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.togglePalette(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue)
                ) {
                    Text("Close")
                }
            },
            containerColor = QuestNavySurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Submit Confirmation Dialog
    if (state.showSubmitDialog) {
        val answeredCount = state.questions.count { it.status == QuestionStatus.ANSWERED }
        val answeredAndMarkedCount = state.questions.count { it.status == QuestionStatus.ANSWERED_AND_MARKED }
        val totalEvaluated = answeredCount + answeredAndMarkedCount
        val unattemptedCount = state.questions.size - totalEvaluated
        val markedReviewOnlyCount = state.questions.count { it.status == QuestionStatus.MARKED_FOR_REVIEW }

        AlertDialog(
            onDismissRequest = viewModel::dismissSubmitConfirmation,
            title = {
                Text(
                    "Submit Mock Test?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Review your test summary before final submission:", color = QuestTextSecondary)
                    Spacer(Modifier.height(4.dp))
                    Text("✅ Evaluated Answers: $totalEvaluated / 50", color = QuestSuccessGreen, fontWeight = FontWeight.Bold)
                    Text("• Answered: $answeredCount", color = QuestTextSecondary, fontSize = 13.sp)
                    Text("• Ans & Marked for Review: $answeredAndMarkedCount", color = QuestAccentAmber, fontSize = 13.sp)
                    Text("❌ Unattempted / Skipped: $unattemptedCount", color = QuestErrorRed, fontWeight = FontWeight.SemiBold)
                    if (markedReviewOnlyCount > 0) {
                        Text("🟣 Marked without Answer: $markedReviewOnlyCount (Will not be evaluated)", color = Color(0xFFBA68C8), fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Note: Answers marked for review with an option selected WILL be evaluated for your MHT-CET score.",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuestAccentGoldLight
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitTest { result ->
                            onFinishTest(
                                result.score,
                                result.percentage,
                                result.accuracy,
                                result.correctCount,
                                result.incorrectCount,
                                result.unattemptedCount,
                                result.timeTakenSeconds,
                                result.classXiScore,
                                result.classXiiScore
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestSuccessGreen)
                ) {
                    Text("Yes, Submit Test")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissSubmitConfirmation) {
                    Text("Continue Test", color = QuestTextSecondary)
                }
            },
            containerColor = QuestNavySurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Exit Confirmation Dialog (Back Button / Gesture)
    if (state.showExitConfirmDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissExitConfirmation,
            title = {
                Text(
                    "Exit Mock Test?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Your 90-minute test is currently running. Would you like to submit your answers now or return to the test?",
                        color = QuestTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitTest { result ->
                            onFinishTest(
                                result.score,
                                result.percentage,
                                result.accuracy,
                                result.correctCount,
                                result.incorrectCount,
                                result.unattemptedCount,
                                result.timeTakenSeconds,
                                result.classXiScore,
                                result.classXiiScore
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuestSuccessGreen)
                ) {
                    Text("Submit Answers")
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onExit) {
                        Text("Discard & Exit", color = QuestErrorRed)
                    }
                    Button(
                        onClick = viewModel::dismissExitConfirmation,
                        colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue)
                    ) {
                        Text("Resume")
                    }
                }
            },
            containerColor = QuestNavySurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun StatusBadge(status: QuestionStatus) {
    val (label, bg, fg) = when (status) {
        QuestionStatus.ANSWERED -> Triple("Answered", QuestSuccessGreen.copy(alpha = 0.2f), QuestSuccessGreen)
        QuestionStatus.NOT_ANSWERED -> Triple("Not Answered", QuestErrorRed.copy(alpha = 0.2f), QuestErrorRed)
        QuestionStatus.MARKED_FOR_REVIEW -> Triple("Marked", ColorMarkedPurple.copy(alpha = 0.2f), Color(0xFFBA68C8))
        QuestionStatus.ANSWERED_AND_MARKED -> Triple("Ans & Marked", ColorAnsweredAndMarked.copy(alpha = 0.2f), QuestAccentAmber)
        QuestionStatus.NOT_VISITED -> Triple("Not Visited", ColorNotVisited, QuestTextTertiary)
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, fg.copy(alpha = 0.4f))
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = fg,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun PaletteLegendItem(label: String, color: Color, hasCheck: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            if (hasCheck) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(QuestSuccessGreen)
                )
            }
        }
        Spacer(Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = QuestTextSecondary)
    }
}
