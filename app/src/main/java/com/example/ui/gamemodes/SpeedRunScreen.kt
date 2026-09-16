package com.example.ui.gamemodes

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.QuestionEntity
import com.example.data.repository.MathRepository
import com.example.ui.components.OptionCard
import com.example.ui.components.QuestMetricCard
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SpeedRunScreen(
    repository: MathRepository,
    onBack: () -> Unit
) {
    var questions by remember { mutableStateOf<List<QuestionEntity>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var combo by remember { mutableIntStateOf(1) }
    var remainingSeconds by remember { mutableIntStateOf(300) } // 5 minutes
    var isGameOver by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        questions = repository.getRandomQuestions(10)
    }

    // Timer countdown
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
                if (remainingSeconds == 0) {
                    isGameOver = true
                }
            }
        }
    }

    val currentQ = questions.getOrNull(currentIndex)

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "⚡ Speed Run Drill",
                onBack = onBack,
                streak = combo
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (isGameOver || (currentIndex >= questions.size && questions.isNotEmpty())) {
                // Game Over Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, QuestAccentGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("⚡", fontSize = 48.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Speed Run Completed!",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestAccentGold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Final Score: $score Points",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = QuestTextPrimary
                        )
                        Text(
                            "Max Combo: ${combo}x Multiplier",
                            style = MaterialTheme.typography.bodyMedium,
                            color = QuestAccentAmber
                        )

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Hub")
                        }
                    }
                }
            } else if (currentQ != null) {
                // Timer & Score header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = QuestAccentGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Combo: ${combo}x Multiplier 🔥",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestAccentGoldLight,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = "⏱️ ${remainingSeconds}s remaining",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (remainingSeconds < 30) QuestErrorRed else QuestTextPrimary
                    )
                }

                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = QuestAccentGold,
                    trackColor = QuestNavySurface
                )

                // Question
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Q ${currentIndex + 1} • ${currentQ.topic}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestAccentGold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = currentQ.question,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                            color = QuestTextPrimary
                        )
                    }
                }

                // Options
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        "A" to currentQ.optionA,
                        "B" to currentQ.optionB,
                        "C" to currentQ.optionC,
                        "D" to currentQ.optionD
                    ).forEach { (letter, text) ->
                        OptionCard(
                            optionLetter = letter,
                            optionText = text,
                            isSelected = selectedOption == letter,
                            isAnswerRevealed = isAnswerSubmitted,
                            isCorrectOption = letter == currentQ.correctAnswer,
                            onClick = {
                                if (!isAnswerSubmitted) {
                                    selectedOption = letter
                                }
                            }
                        )
                    }
                }

                if (!isAnswerSubmitted) {
                    Button(
                        onClick = {
                            val right = selectedOption == currentQ.correctAnswer
                            isCorrect = right
                            isAnswerSubmitted = true
                            if (right) {
                                score += (10 * combo)
                                combo += 1
                            } else {
                                combo = 1
                            }
                            coroutineScope.launch {
                                repository.recordQuestionAttempt(
                                    question = currentQ,
                                    selectedOption = selectedOption ?: "",
                                    isCorrect = right,
                                    timeSeconds = 20,
                                    mistakeType = "Time pressure"
                                )
                            }
                        },
                        enabled = selectedOption != null,
                        colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("speed_run_submit")
                    ) {
                        Text("Confirm Answer", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentIndex < questions.size - 1) {
                                currentIndex++
                                selectedOption = null
                                isAnswerSubmitted = false
                                isCorrect = false
                            } else {
                                isGameOver = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = QuestAccentGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("speed_run_next")
                    ) {
                        Text("Next Drill Question →", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = QuestAccentGold)
                }
            }
        }
    }
}
