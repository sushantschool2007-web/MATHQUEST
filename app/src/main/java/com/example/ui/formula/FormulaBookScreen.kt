package com.example.ui.formula

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.components.MathFormulaRenderer
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

@Composable
fun FormulaBookScreen(
    viewModel: FormulaBookViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isDark = ThemeController.isDarkTheme

    Scaffold(
        containerColor = if (isDark) DarkSpectrumBackground else LightSpectrumBackground,
        topBar = {
            QuestTopBar(
                title = "Formula Vault",
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

            // Search Bar with Instant Filtering by Keyword, Topic, or Exam Tags (#MHT-CET, #JEE)
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = {
                    Text(
                        "Search formula, topic, #MHT-CET, #JEE...",
                        color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary,
                    unfocusedBorderColor = if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                    focusedTextColor = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                    unfocusedTextColor = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                    focusedContainerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard,
                    unfocusedContainerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("formula_search_input")
            )

            Spacer(Modifier.height(10.dp))

            // Tag & Filter Chips row
            val scrollState = rememberScrollState()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                listOf(
                    FormulaFilter.ALL to "All Formulas",
                    FormulaFilter.TAG_MHT_CET to "#MHT-CET",
                    FormulaFilter.TAG_JEE to "#JEE Main",
                    FormulaFilter.BOOKMARKED to "Saved Revision ⭐",
                    FormulaFilter.CLASS_12 to "Std 12",
                    FormulaFilter.CLASS_11 to "Std 11"
                ).forEach { (filter, label) ->
                    val isSelected = state.activeFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setFilter(filter) },
                        label = {
                            Text(
                                label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard,
                            labelColor = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) Color.Transparent else (if (isDark) DarkSpectrumBorder else LightSpectrumBorder)
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Formula List with LaTeX rendered math
            if (state.formulas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📐", fontSize = 36.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No formulas found matching '${state.searchQuery}'",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                        )
                    }
                }
            } else {
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
}

@Composable
private fun FormulaCard(
    formula: FormulaEntity,
    onBookmarkToggle: () -> Unit
) {
    val isDark = ThemeController.isDarkTheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                RoundedCornerShape(16.dp)
            )
            .testTag("formula_card_${formula.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Std ${formula.stdClass} • ${formula.chapter}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            color = (if (isDark) DarkSpectrumSuccess else LightSpectrumEmerald).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "#MHT-CET",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = formula.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                    )
                }

                // Quick-bookmark toggle button for rapid exam revision
                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.testTag("bookmark_toggle_${formula.id}")
                ) {
                    Icon(
                        imageVector = if (formula.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Quick Bookmark Revision",
                        tint = if (formula.isBookmarked) (if (isDark) DarkSpectrumAmber else Color(0xFFD97706)) else (if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Clean, high-legibility rendered math equations using standard LaTeX formatting
            MathFormulaRenderer(
                rawFormula = formula.formula,
                isDark = isDark
            )

            Spacer(Modifier.height(10.dp))

            if (formula.whenToUse.isNotBlank()) {
                Text(
                    text = "When to Use: ${formula.whenToUse}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                )
                Spacer(Modifier.height(4.dp))
            }

            if (formula.shortcut.isNotBlank()) {
                Surface(
                    color = (if (isDark) DarkSpectrumAmber else Color(0xFFEA580C)).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        (if (isDark) DarkSpectrumAmber else Color(0xFFEA580C)).copy(alpha = 0.35f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚡ Exam Shortcut: ${formula.shortcut}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (isDark) DarkSpectrumAmber else Color(0xFFC2410C),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
