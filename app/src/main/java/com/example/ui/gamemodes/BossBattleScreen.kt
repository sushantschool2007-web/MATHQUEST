package com.example.ui.gamemodes

import androidx.compose.animation.*
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

@Composable
fun BossBattleScreen(
    repository: MathRepository,
    onBack: () -> Unit
) {
    var bossHp by remember { mutableIntStateOf(100) }
    var playerHearts by remember { mutableIntStateOf(5) }
    var questions by remember { mutableStateOf<List<QuestionEntity>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var battleStatus by remember { mutableStateOf<String?>(null) } // "VICTORY", "DEFEAT"

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        questions = repository.getRandomQuestions(6)
    }

    val currentQ = questions.getOrNull(currentIndex)

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "⚔️ BOSS BATTLE",
                onBack = onBack,
                hearts = playerHearts
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
            if (battleStatus == "VICTORY") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, QuestAccentGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏆", fontSize = 52.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "BOSS DEFEATED!",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = QuestAccentGold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "You vanquished the Trigonometry Titan!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = QuestTextSecondary
                        )
                        Spacer(Modifier.height(14.dp))
                        Surface(
                            color = QuestAccentGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "+250 XP Awarded! ⚡",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = QuestAccentGoldLight,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Claim Reward & Return")
                        }
                    }
                }
            } else if (battleStatus == "DEFEAT") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, QuestErrorRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("💀", fontSize = 52.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "QUEST FAILED",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = QuestErrorRed
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "You ran out of hearts. Revise the chapter formulas and challenge the boss again!",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuestTextSecondary
                        )
                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Retreat to Safety")
                        }
                    }
                }
            } else if (currentQ != null) {
                // Boss Status Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, QuestErrorRed.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👹", fontSize = 28.sp)
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(
                                        "Trigonometry Titan",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = QuestTextPrimary
                                    )
                                    Text(
                                        "Chapter Boss • Rank A",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = QuestErrorRed
                                    )
                                }
                            }

                            Text(
                                text = "HP $bossHp/100",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestErrorRed
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { bossHp.toFloat() / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape),
                            color = QuestErrorRed,
                            trackColor = QuestNavyDark
                        )
                    }
                }

                // Question Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Strike the Titan! Answer correctly to deal -25 Boss HP.",
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
                            isAnswerSubmitted = true
                            if (right) {
                                val nextHp = bossHp - 25
                                bossHp = maxOf(0, nextHp)
                                if (bossHp == 0) {
                                    battleStatus = "VICTORY"
                                    coroutineScope.launch {
                                        repository.saveMockTestResult(
                                            com.example.data.local.entity.MockTestResultEntity(
                                                score = 250,
                                                percentage = 100f,
                                                accuracy = 100f,
                                                correctCount = 4,
                                                incorrectCount = 0,
                                                unattemptedCount = 0,
                                                totalQuestions = 4,
                                                classXiScore = 50,
                                                classXiiScore = 200,
                                                timeTakenSeconds = 120,
                                                fastestQuestionTimeSeconds = 20,
                                                slowestQuestionTimeSeconds = 40,
                                                strongTopics = "Trigonometry",
                                                weakTopics = "None",
                                                recommendedRevision = "Ready for Boss Mastery",
                                                prepLevelEstimate = "Master"
                                            )
                                        )
                                    }
                                }
                            } else {
                                playerHearts -= 1
                                if (playerHearts <= 0) {
                                    battleStatus = "DEFEAT"
                                }
                            }
                        },
                        enabled = selectedOption != null,
                        colors = ButtonDefaults.buttonColors(containerColor = QuestPrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("boss_attack_button")
                    ) {
                        Text("⚔️ Strike Boss", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentIndex < questions.size - 1 && battleStatus == null) {
                                currentIndex++
                                selectedOption = null
                                isAnswerSubmitted = false
                            } else if (battleStatus == null) {
                                if (bossHp <= 25) {
                                    battleStatus = "VICTORY"
                                } else {
                                    battleStatus = "DEFEAT"
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = QuestAccentGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("boss_next_round_button")
                    ) {
                        Text("Next Round →", color = Color.Black, fontWeight = FontWeight.Bold)
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
