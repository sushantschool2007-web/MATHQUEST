package com.example.ui.chapter

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ComprehensiveChapter
import com.example.data.model.SubtopicItem
import com.example.data.model.ValidationReport
import com.example.ui.components.QuestTopBar
import com.example.ui.theme.*

enum class ChapterTopicStatus(val label: String, val color: Color) {
    NOT_STARTED("Not Started", Color(0xFF64748B)),
    IN_PROGRESS("In Progress", Color(0xFFF59E0B)),
    COMPLETED("Completed", Color(0xFF10B981))
}

@Composable
fun ChapterListScreen(
    viewModel: ChapterListViewModel,
    onBack: () -> Unit,
    onChapterSelect: (String) -> Unit,
    onJumpToConcepts: ((String) -> Unit)? = null,
    onJumpToFormulas: ((String) -> Unit)? = null,
    onJumpToPractice: ((String) -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isDark = ThemeController.isDarkTheme

    Scaffold(
        containerColor = if (isDark) DarkSpectrumBackground else LightSpectrumBackground,
        topBar = {
            QuestTopBar(
                title = "Chapter & Topic Breakdown",
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

            // Search Bar & Validation Tool Trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    placeholder = {
                        Text(
                            "Search chapters, subtopics...",
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
                        .weight(1f)
                        .testTag("chapter_search_input")
                )

                IconButton(
                    onClick = { viewModel.toggleValidationDialog(true) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) DarkSpectrumCard else LightSpectrumCard)
                        .border(
                            1.dp,
                            if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("syllabus_validation_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Audit Syllabus",
                        tint = if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Filter Tabs (Std 11 Part 1, Std 11 Part 2, Std 12 Part 1, Std 12 Part 2, MHT-CET, JEE Main)
            val scrollState = rememberScrollState()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                SyllabusViewMode.entries.forEach { mode ->
                    val isSelected = state.selectedMode == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectMode(mode) },
                        label = {
                            Text(
                                text = mode.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard,
                            labelColor = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary,
                            selectedContainerColor = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) Color.Transparent else (if (isDark) DarkSpectrumBorder else LightSpectrumBorder),
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Summary row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.chapters.size} Chapters in Syllabus",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary
                )
                Text(
                    text = "Hierarchy: Std → Part → Chapter → Subtopic",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                    fontSize = 10.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.chapters, key = { it.chapter.chapterId }) { item ->
                    ExpandableChapterCard(
                        model = item,
                        onChapterSelect = { onChapterSelect(item.chapter.name) },
                        onJumpToConcepts = {
                            if (onJumpToConcepts != null) onJumpToConcepts(item.chapter.name)
                            else onChapterSelect(item.chapter.name)
                        },
                        onJumpToFormulas = {
                            if (onJumpToFormulas != null) onJumpToFormulas(item.chapter.name)
                            else onChapterSelect(item.chapter.name)
                        },
                        onJumpToPractice = {
                            if (onJumpToPractice != null) onJumpToPractice(item.chapter.name)
                            else onChapterSelect(item.chapter.name)
                        }
                    )
                }
                item {
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }

    if (state.showValidationDialog) {
        ValidationReportDialog(
            report = state.validationReport,
            onDismiss = { viewModel.toggleValidationDialog(false) }
        )
    }
}

/**
 * Expandable list hierarchy (Standard → Part → Chapter → Subtopic)
 * with Status Badges (Not Started, In Progress, Completed) and
 * Direct Action Buttons to jump into Concepts, Formula Vault, or Practice Questions.
 */
@Composable
private fun ExpandableChapterCard(
    model: ComprehensiveChapterUiModel,
    onChapterSelect: () -> Unit,
    onJumpToConcepts: () -> Unit,
    onJumpToFormulas: () -> Unit,
    onJumpToPractice: () -> Unit
) {
    val ch = model.chapter
    val progress = model.progress
    val mastery = progress?.mastery ?: 0
    val isDark = ThemeController.isDarkTheme

    var isExpanded by remember { mutableStateOf(false) }

    val overallStatus = when {
        mastery >= 80 -> ChapterTopicStatus.COMPLETED
        mastery > 0 -> ChapterTopicStatus.IN_PROGRESS
        else -> ChapterTopicStatus.NOT_STARTED
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                1.dp,
                if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                RoundedCornerShape(18.dp)
            )
            .testTag("chapter_item_${ch.chapterId}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Standard → Part → Chapter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Hierarchy badges: Std -> Part -> Chapter
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Std ${ch.standard}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text("→", fontSize = 11.sp, color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary)

                    Surface(
                        color = (if (isDark) DarkSpectrumActionElectric else LightSpectrumActionIndigo).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Part ${ch.part}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumActionElectric else LightSpectrumActionIndigo,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text("→", fontSize = 11.sp, color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary)

                    Surface(
                        color = (if (isDark) DarkSpectrumBorder else Color(0xFFE2E8F0)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Ch ${ch.chapterNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Status Badge: Not Started, In Progress, Completed
                Surface(
                    color = overallStatus.color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, overallStatus.color.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = overallStatus.label,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = overallStatus.color,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Chapter Title & Description
            Text(
                text = ch.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = ch.description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary,
                maxLines = if (isExpanded) 10 else 2
            )

            Spacer(Modifier.height(12.dp))

            // Direct Action Buttons: Concepts, Formula Vault, Practice Questions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onJumpToConcepts,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Concepts", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onJumpToFormulas,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isDark) DarkSpectrumActionElectric else LightSpectrumActionIndigo
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Formulas", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onJumpToPractice,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Practice", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(10.dp))

            // Expandable Subtopic Hierarchy toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SUBTOPICS (${ch.subtopics.size})",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isExpanded) "Tap to collapse" else "Tap to expand hierarchy",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary,
                        fontSize = 10.sp
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ch.subtopics.forEachIndexed { index, subtopic ->
                        val subtopicStatus = when {
                            mastery >= 80 -> ChapterTopicStatus.COMPLETED
                            mastery > 0 && index == 0 -> ChapterTopicStatus.IN_PROGRESS
                            else -> ChapterTopicStatus.NOT_STARTED
                        }

                        SubtopicRowItem(
                            index = index + 1,
                            subtopic = subtopic,
                            status = subtopicStatus,
                            onClick = onChapterSelect
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubtopicRowItem(
    index: Int,
    subtopic: SubtopicItem,
    status: ChapterTopicStatus,
    onClick: () -> Unit
) {
    val isDark = ThemeController.isDarkTheme

    Surface(
        color = if (isDark) DarkSpectrumSurface else LightSpectrumSurface,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDark) DarkSpectrumBorder else LightSpectrumBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    color = (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary).copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$index",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = subtopic.name,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                    )
                    Text(
                        text = "ID: ${subtopic.id}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary,
                        fontSize = 9.sp
                    )
                }
            }

            // Subtopic Status Badge
            Surface(
                color = status.color.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = status.label,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = status.color,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ValidationReportDialog(
    report: ValidationReport,
    onDismiss: () -> Unit
) {
    val isDark = ThemeController.isDarkTheme

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Syllabus Audit & Coverage",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Strict adherence to Maharashtra State Board syllabus:",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                )
                Text(
                    text = "• Class 11 Chapters: ${report.std11Part1Count + report.std11Part2Count} (Part 1: ${report.std11Part1Count}, Part 2: ${report.std11Part2Count})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                )
                Text(
                    text = "• Class 12 Chapters: ${report.std12Part1Count + report.std12Part2Count} (Part 1: ${report.std12Part1Count}, Part 2: ${report.std12Part2Count})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                )
                Text(
                    text = "• Total Verified Subtopics: ${report.totalSubtopics}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                )
                Text(
                    text = "• Total Verified Formulas: ${report.totalFormulas} formulas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess
                )
                Text(
                    text = "• Step-by-Step Solved Examples: ${report.totalSolvedExamples}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary)
            }
        },
        containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
    )
}
