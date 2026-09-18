package com.example.ui.chapter

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import com.example.data.model.ComprehensiveChapter
import com.example.ui.components.*
import com.example.ui.quiz.QuizQuestionPanel
import com.example.ui.theme.*

@Composable
fun ChapterLearningScreen(
    viewModel: ChapterLearningViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val ch = state.comprehensiveChapter
    val isDark = ThemeController.isDarkTheme

    Scaffold(
        containerColor = if (isDark) DarkSpectrumBackground else LightSpectrumBackground,
        topBar = {
            QuestTopBar(
                title = ch?.name ?: state.chapterName,
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
            // Chapter Metadata Card Header
            if (ch != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Std ${ch.standard} • Part ${ch.part} • Chapter ${ch.chapterNumber}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                            )
                            Surface(
                                color = (if (isDark) DarkSpectrumAmber else Color(0xFFD97706)).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Difficulty: ${ch.difficultyLevel}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) DarkSpectrumAmber else Color(0xFFB45309),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = ch.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                        )

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = if (isDark) DarkSpectrumSurface else LightSpectrumSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        "MHT-CET Relevance",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        ch.mhtCetRelevance,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                                        fontSize = 11.sp,
                                        maxLines = 2
                                    )
                                }
                            }

                            Surface(
                                color = if (isDark) DarkSpectrumSurface else LightSpectrumSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        "JEE Main Relevance",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isDark) DarkSpectrumActionElectric else LightSpectrumActionIndigo,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        ch.jeeMainRelevance,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                                        fontSize = 11.sp,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Tab switch: Subtopics | Formulas | Solved Examples | Practice Arena
            ScrollableTabRow(
                selectedTabIndex = state.activeTab,
                containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard,
                contentColor = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary,
                edgePadding = 8.dp,
                indicator = { tabPositions ->
                    if (state.activeTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[state.activeTab]),
                            color = if (isDark) DarkSpectrumActionPrimary else LightSpectrumActionPrimary
                        )
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder,
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Tab(
                    selected = state.activeTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    text = {
                        Text(
                            "Concepts & Subtopics",
                            fontSize = 12.sp,
                            fontWeight = if (state.activeTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (state.activeTab == 0) (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary) else (if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary)
                        )
                    }
                )
                Tab(
                    selected = state.activeTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    text = {
                        Text(
                            "Formulas & Shortcuts",
                            fontSize = 12.sp,
                            fontWeight = if (state.activeTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (state.activeTab == 1) (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary) else (if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary)
                        )
                    }
                )
                Tab(
                    selected = state.activeTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    text = {
                        Text(
                            "Solved Examples",
                            fontSize = 12.sp,
                            fontWeight = if (state.activeTab == 2) FontWeight.Bold else FontWeight.Normal,
                            color = if (state.activeTab == 2) (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary) else (if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary)
                        )
                    }
                )
                Tab(
                    selected = state.activeTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    text = {
                        Text(
                            "Quiz & Practice",
                            fontSize = 12.sp,
                            fontWeight = if (state.activeTab == 3) FontWeight.Bold else FontWeight.Normal,
                            color = if (state.activeTab == 3) (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary) else (if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary)
                        )
                    }
                )
            }

            // Tab Content
            when (state.activeTab) {
                0 -> SubtopicsSection(ch = ch, isDark = isDark)
                1 -> FormulasSection(ch = ch, dbFormulas = state.formulas, isDark = isDark)
                2 -> SolvedExamplesSection(ch = ch, isDark = isDark)
                3 -> {
                    if (state.questions.isNotEmpty()) {
                        val currentQ = state.questions[state.currentQuestionIndex]
                        QuizQuestionPanel(
                            question = currentQ,
                            currentIndex = state.currentQuestionIndex,
                            totalQuestions = state.questions.size,
                            remainingSeconds = null,
                            selectedOption = state.selectedOption,
                            isSubmitted = state.isAnswerSubmitted,
                            isCorrect = state.isCorrect,
                            onOptionSelected = viewModel::selectOption,
                            onSubmit = viewModel::submitAnswer,
                            onNext = viewModel::nextQuestion,
                            isDark = isDark
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Preparing practice questions...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                            )
                        }
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
private fun SubtopicsSection(ch: ComprehensiveChapter?, isDark: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "COMPLETE SUBTOPICS HIERARCHY",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
        )

        if (ch != null) {
            ch.subtopics.forEachIndexed { index, sub ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary).copy(alpha = 0.15f),
                            shape = CircleShape,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sub.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                            )
                            Text(
                                text = "ID: ${sub.id}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "KEY CONCEPTS & THEOREMS",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
            )

            ch.importantConcepts.forEach { concept ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            "•",
                            color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = concept,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormulasSection(
    ch: ComprehensiveChapter?,
    dbFormulas: List<com.example.data.local.entity.FormulaEntity>,
    isDark: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "VERIFIED FORMULAS & SHORTCUTS",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
        )

        val catalogFormulas = ch?.importantFormulas ?: emptyList()

        if (catalogFormulas.isEmpty() && dbFormulas.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Core identities for ${ch?.name ?: "this chapter"}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                    )
                }
            }
        } else {
            catalogFormulas.forEach { f ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = f.formulaName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                            )
                            Surface(
                                color = (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = f.topicId,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // LaTeX formatted math
                        MathFormulaRenderer(
                            rawFormula = f.formula,
                            isDark = isDark
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = f.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
                        )

                        if (f.variables.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Variables: ${f.variables}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDark) DarkSpectrumTextTertiary else LightSpectrumTextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SolvedExamplesSection(ch: ComprehensiveChapter?, isDark: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "STEP-BY-STEP SOLVED EXAMPLES",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary
        )

        val examples = ch?.solvedExamples ?: emptyList()
        if (examples.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No examples available for this chapter.",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            examples.forEach { ex ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) DarkSpectrumCard else LightSpectrumCard
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ex.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                            )
                            if (ex.keyConcept.isNotBlank()) {
                                Surface(
                                    color = (if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = ex.keyConcept,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isDark) DarkSpectrumSuccess else LightSpectrumSuccess,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Surface(
                            color = if (isDark) DarkSpectrumSurface else LightSpectrumSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isDark) DarkSpectrumBorder else LightSpectrumBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Problem:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                                )
                                Text(
                                    text = formatLatexToReadableMath(ex.problem),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isDark) DarkSpectrumTextPrimary else LightSpectrumTextPrimary
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = "Solution & Method:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumActionElectric else LightSpectrumActionIndigo
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = formatLatexToReadableMath(ex.solution),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkSpectrumTextSecondary else LightSpectrumTextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
