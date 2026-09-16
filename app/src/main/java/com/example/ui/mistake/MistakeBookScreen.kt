package com.example.ui.mistake

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.QuestTopBar
import com.example.ui.components.TeachMeDialog
import com.example.ui.theme.*

@Composable
fun MistakeBookScreen(
    viewModel: MistakeBookViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "Mistake Notebook",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Subtitle & Filter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.mistakeItems.size} Mistakes Recorded",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = QuestTextSecondary
                )

                FilterChip(
                    selected = state.showResolved,
                    onClick = viewModel::toggleShowResolved,
                    label = { Text("Include Resolved") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = QuestPrimaryBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            if (state.mistakeItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎉", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "No Unresolved Mistakes!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextPrimary
                        )
                        Text(
                            "Great job! Solve more questions or mock tests to find areas for improvement.",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuestTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.mistakeItems, key = { it.mistake.id }) { item ->
                        val mistake = item.mistake
                        val q = item.question

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, QuestErrorRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .testTag("mistake_card_${mistake.id}"),
                            colors = CardDefaults.cardColors(containerColor = QuestNavyCard)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = QuestErrorRed.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = mistake.mistakeType,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = QuestErrorRed,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Text(
                                        text = mistake.chapter,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = QuestTextSecondary
                                    )
                                }

                                Spacer(Modifier.height(10.dp))

                                if (q != null) {
                                    Text(
                                        text = q.question,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            lineHeight = 22.sp
                                        ),
                                        color = QuestTextPrimary
                                    )

                                    Spacer(Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Surface(
                                            color = QuestErrorRed.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(8.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, QuestErrorRed.copy(alpha = 0.5f)),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = "Your Choice: Option ${mistake.selectedOption}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = QuestErrorRed,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }

                                        Surface(
                                            color = QuestSuccessGreen.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(8.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, QuestSuccessGreen.copy(alpha = 0.5f)),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = "Correct: Option ${mistake.correctOption}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = QuestSuccessGreen,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { viewModel.openTeachMe(q) },
                                            shape = RoundedCornerShape(10.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = QuestPrimaryBlueLight),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("🎓 Teach Me", fontSize = 12.sp)
                                        }

                                        if (!mistake.isResolved) {
                                            Button(
                                                onClick = { viewModel.markResolved(mistake.id) },
                                                colors = ButtonDefaults.buttonColors(containerColor = QuestSuccessGreen),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.weight(1.2f)
                                            ) {
                                                Text("Mark Resolved ✓", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    // Teach Me Dialog
    val activeQ = state.activeTeachMeQuestion
    if (activeQ != null) {
        TeachMeDialog(
            given = activeQ.teachMeGiven,
            weNeed = activeQ.teachMeWeNeed,
            formula = activeQ.teachMeFormula,
            substitution = activeQ.teachMeSubstitution,
            calculation = activeQ.teachMeCalculation,
            finalAnswer = activeQ.teachMeFinalAnswer,
            shortcut = activeQ.teachMeShortcut,
            onDismiss = viewModel::dismissTeachMe
        )
    }
}
