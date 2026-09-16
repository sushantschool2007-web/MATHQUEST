package com.example.ui.chapter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
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
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

@Composable
fun ChapterListScreen(
    viewModel: ChapterListViewModel,
    onBack: () -> Unit,
    onChapterSelect: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "MHT-CET Chapters",
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

            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = { Text("Search chapters, topics...", color = QuestTextTertiary) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = QuestTextSecondary)
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = QuestPrimaryBlue,
                    unfocusedBorderColor = QuestNavyBorder,
                    focusedTextColor = QuestTextPrimary,
                    unfocusedTextColor = QuestTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chapter_search_input")
            )

            Spacer(Modifier.height(14.dp))

            // Tabs: Std. XII (80% Weightage) vs Std. XI (20% Weightage)
            TabRow(
                selectedTabIndex = if (state.selectedClassTab == 12) 0 else 1,
                containerColor = QuestNavyCard,
                contentColor = QuestAccentGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[if (state.selectedClassTab == 12) 0 else 1]),
                        color = QuestAccentGold
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, QuestNavyBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = state.selectedClassTab == 12,
                    onClick = { viewModel.selectTab(12) },
                    text = {
                        Text(
                            "Std. XII (80% Weight)",
                            fontWeight = if (state.selectedClassTab == 12) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = state.selectedClassTab == 11,
                    onClick = { viewModel.selectTab(11) },
                    text = {
                        Text(
                            "Std. XI (20% Weight)",
                            fontWeight = if (state.selectedClassTab == 11) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            Spacer(Modifier.height(14.dp))

            val currentList = if (state.selectedClassTab == 12) state.chaptersXii else state.chaptersXi

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(currentList, key = { it.meta.name }) { item ->
                    ChapterCard(
                        model = item,
                        onClick = { onChapterSelect(item.meta.name) }
                    )
                }
                item {
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ChapterCard(
    model: ChapterUiModel,
    onClick: () -> Unit
) {
    val meta = model.meta
    val progress = model.progress
    val mastery = progress?.mastery ?: 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, QuestNavyBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("chapter_item_${meta.name.replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = QuestNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${meta.icon} ${meta.name}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "CET Weight: ${meta.weightageCetMarks} marks (~${meta.weightageCetMarks / 2} Qs)",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuestAccentGoldLight
                    )
                }

                Surface(
                    color = if (mastery >= 80) QuestSuccessGreen.copy(alpha = 0.2f) else QuestPrimaryBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (mastery >= 80) QuestSuccessGreen else QuestPrimaryBlue
                    )
                ) {
                    Text(
                        text = "$mastery% Mastery",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (mastery >= 80) QuestSuccessGreen else QuestPrimaryBlueLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { mastery.toFloat() / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (mastery >= 80) QuestSuccessGreen else QuestPrimaryBlue,
                trackColor = QuestNavySurface
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${progress?.questionsSolved ?: 0} Questions Solved",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuestTextSecondary
                )

                Text(
                    text = "Practice →",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = QuestPrimaryBlueLight
                )
            }
        }
    }
}
