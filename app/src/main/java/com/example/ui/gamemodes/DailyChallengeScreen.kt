package com.example.ui.gamemodes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DailyChallengeScreen(
    repository: MathRepository,
    onBack: () -> Unit
) {
    val todayKey = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    val todayFormatted = remember { SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date()) }

    var questions by remember { mutableStateOf<List<QuestionEntity>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        questions = repository.getRandomQuestions(5)
    }

    val currentQ = questions.getOrNull(currentIndex)

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "🌟 Daily Challenge",
                onBack = onBack
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
            if (isCompleted || (currentIndex >= questions.size && questions.isNotEmpty())) {
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
                        Text("🌟", fontSize = 52.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Daily Challenge Complete!",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestAccentGold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "You scored $score / ${questions.size}",
                            style = MaterialTheme.typography.titleMedium,
                            color = QuestTextPrimary
                        )
                        Spacer(Modifier.height(14.dp))
                        Surface(
                            color = QuestAccentGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "+100 Bonus XP & Streak Extended! 🔥",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = QuestAccentGoldLight,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Quest Hub")
                        }
                    }
                }
            } else if (currentQ != null) {
                // Header date & progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todayFormatted,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestAccentGold
                    )
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuestTextSecondary
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

                // Question Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${currentQ.chapter} • ${currentQ.topic}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = QuestPrimaryBlueLight
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
                                score++
                            }
                            coroutineScope.launch {
                                repository.recordQuestionAttempt(
                                    question = currentQ,
                                    selectedOption = selectedOption ?: "",
                                    isCorrect = right,
                                    timeSeconds = 30
                                )
                            }
                        },
                        enabled = selectedOption != null,
                        colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("daily_challenge_submit")
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
                                isCompleted = true
                                coroutineScope.launch {
                                    repository.completeDailyChallenge(todayKey, score, 100)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = QuestAccentGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("daily_challenge_next")
                    ) {
                        Text(
                            if (currentIndex < questions.size - 1) "Next Question →" else "Complete Challenge 🌟",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
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
