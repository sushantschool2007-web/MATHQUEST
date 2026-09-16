package com.example.ui.formula

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
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
import com.example.data.local.entity.FormulaEntity
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

@Composable
fun FormulaBookScreen(
    viewModel: FormulaBookViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            QuestTopBar(
                title = "MHT-CET Formula Book",
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
                placeholder = { Text("Search formulas, chapters...", color = QuestTextTertiary) },
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
                    .testTag("formula_search_input")
            )

            Spacer(Modifier.height(10.dp))

            // Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    FormulaFilter.ALL to "All",
                    FormulaFilter.CLASS_12 to "Std. XII",
                    FormulaFilter.CLASS_11 to "Std. XI",
                    FormulaFilter.BOOKMARKED to "Saved ⭐"
                ).forEach { (filter, label) ->
                    val isSelected = state.activeFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setFilter(filter) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = QuestPrimaryBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.formulas, key = { it.id }) { formula ->
                    FormulaCard(
                        formula = formula,
                        onBookmarkToggle = { viewModel.toggleBookmark(formula) }
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
private fun FormulaCard(
    formula: FormulaEntity,
    onBookmarkToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, QuestNavyBorder, RoundedCornerShape(16.dp))
            .testTag("formula_card_${formula.id}"),
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
                        text = "${formula.chapter} (Std. ${formula.stdClass})",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestAccentGoldLight
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = formula.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                }

                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.testTag("bookmark_toggle_${formula.id}")
                ) {
                    Icon(
                        imageVector = if (formula.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (formula.isBookmarked) QuestAccentGold else QuestTextTertiary
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Surface(
                color = QuestNavyDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuestPrimaryBlue.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formula.formula,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    ),
                    color = QuestTextPrimary,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            if (formula.whenToUse.isNotBlank()) {
                Text(
                    text = "When to Use: ${formula.whenToUse}",
                    style = MaterialTheme.typography.bodySmall,
                    color = QuestTextSecondary
                )
                Spacer(Modifier.height(4.dp))
            }

            if (formula.shortcut.isNotBlank()) {
                Surface(
                    color = QuestAccentGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestAccentGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚡ CET Shortcut: ${formula.shortcut}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = QuestAccentGoldLight,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
