package com.example.ui.admin

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.StudentEntity
import com.example.data.model.MathematicsRepositoryCatalog
import com.example.ui.components.MathFormulaRenderer
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel,
    onSwitchToStudentView: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isDark = ThemeController.isDarkTheme

    val backgroundColor = if (isDark) QuestNavyDark else LightSpectrumBackground
    val cardColor = if (isDark) QuestNavyCard else LightSpectrumCard
    val borderColor = if (isDark) QuestNavyBorder else LightSpectrumBorder
    val textPrimary = if (isDark) QuestTextPrimary else LightSpectrumTextPrimary
    val textSecondary = if (isDark) QuestTextSecondary else LightSpectrumTextSecondary

    // Snackbar host state
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFFA855F7)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 18.sp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ADMIN DASHBOARD",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = textPrimary
                            )
                            Text(
                                text = "Student Progress & Content Management",
                                style = MaterialTheme.typography.labelSmall,
                                color = textSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onSwitchToStudentView,
                        modifier = Modifier.testTag("admin_back_to_student_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to User Dashboard",
                            tint = textPrimary
                        )
                    }
                },
                actions = {
                    // Switch to Student Dashboard Button
                    Button(
                        onClick = onSwitchToStudentView,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) QuestPrimaryBlue else LightSpectrumActionPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("switch_to_user_dashboard_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Student View",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    // Theme toggle
                    IconButton(
                        onClick = { ThemeController.toggleTheme() },
                        modifier = Modifier.testTag("admin_theme_toggle")
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (isDark) Color(0xFFFBBF24) else Color(0xFF4F46E5)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Admin Tabs
            ScrollableTabRow(
                selectedTabIndex = state.activeTab,
                containerColor = if (isDark) QuestNavySurface else LightSpectrumSurface,
                contentColor = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                edgePadding = 12.dp
            ) {
                Tab(
                    selected = state.activeTab == 0,
                    onClick = { viewModel.setActiveTab(0) },
                    text = {
                        Text(
                            "Students & Progress (${state.students.size})",
                            fontWeight = if (state.activeTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = state.activeTab == 1,
                    onClick = { viewModel.setActiveTab(1) },
                    text = {
                        Text(
                            "User Logins & Emails (${state.loginHistory.size})",
                            fontWeight = if (state.activeTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = state.activeTab == 2,
                    onClick = { viewModel.setActiveTab(2) },
                    text = {
                        Text(
                            "Add Content",
                            fontWeight = if (state.activeTab == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = state.activeTab == 3,
                    onClick = { viewModel.setActiveTab(3) },
                    text = {
                        Text(
                            "Analytics",
                            fontWeight = if (state.activeTab == 3) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            // Tab Content
            when (state.activeTab) {
                0 -> StudentRecordsSection(
                    state = state,
                    viewModel = viewModel,
                    isDark = isDark,
                    cardColor = cardColor,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
                1 -> UserLoginsSection(
                    state = state,
                    viewModel = viewModel,
                    isDark = isDark,
                    cardColor = cardColor,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
                2 -> ContentManagementSection(
                    state = state,
                    viewModel = viewModel,
                    isDark = isDark,
                    cardColor = cardColor,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
                3 -> CohortAnalyticsSection(
                    state = state,
                    isDark = isDark,
                    cardColor = cardColor,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
        }
    }

    // Detailed Student Inspection Dialog
    state.selectedStudent?.let { student ->
        StudentDetailDialog(
            student = student,
            isDark = isDark,
            onDismiss = { viewModel.selectStudent(null) },
            onUpdateNotes = { notes -> viewModel.updateStudentNotes(student.studentId, notes) },
            onAwardXp = { bonus -> viewModel.awardBonusXp(student.studentId, bonus) }
        )
    }
}

@Composable
private fun StudentRecordsSection(
    state: AdminDashboardUiState,
    viewModel: AdminDashboardViewModel,
    isDark: Boolean,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    val students = state.students
    val avgAccuracy = if (students.isNotEmpty()) students.map { it.accuracy }.average().toFloat() else 0f
    val totalSolvedBatch = students.sumOf { it.totalSolved }
    val topPerformer = students.maxByOrNull { it.xp }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary KPI Cards Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(
                    title = "Enrolled",
                    value = "${students.size}",
                    icon = "🎓",
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                AdminStatCard(
                    title = "Avg Accuracy",
                    value = "${"%.1f".format(avgAccuracy)}%",
                    icon = "⚡",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                AdminStatCard(
                    title = "Qs Solved",
                    value = "$totalSolvedBatch",
                    icon = "🎯",
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                AdminStatCard(
                    title = "Top Student",
                    value = topPerformer?.name?.split(" ")?.firstOrNull() ?: "-",
                    icon = "🏆",
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search by name, email, or exam target...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_student_search_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                    unfocusedBorderColor = borderColor
                ),
                singleLine = true
            )
        }

        // Filter Chips
        item {
            val filters = listOf("All", "Class 12", "Class 11", "Top Performers", "Needs Support")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = state.classFilter == filter,
                        onClick = { viewModel.setClassFilter(filter) },
                        label = { Text(filter, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (isDark) QuestPrimaryBlue else LightSpectrumActionPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Students Roster List
        if (state.filteredStudents.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 32.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No student records found matching the query.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textSecondary
                        )
                    }
                }
            }
        } else {
            items(state.filteredStudents, key = { it.studentId }) { student ->
                StudentRosterCard(
                    student = student,
                    isDark = isDark,
                    cardColor = cardColor,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    onInspect = { viewModel.selectStudent(student) }
                )
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDark) QuestNavyCard else LightSpectrumCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 18.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = if (isDark) QuestTextSecondary else LightSpectrumTextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun StudentRosterCard(
    student: StudentEntity,
    isDark: Boolean,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onInspect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onInspect() }
            .testTag("student_card_${student.studentId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = student.name.firstOrNull()?.toString() ?: "S",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = student.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDark) Color(0xFF0F172A) else Color(0xFFEFF6FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = student.email,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))

                Surface(
                    color = if (student.standard == 12) Color(0xFF6366F1).copy(alpha = 0.2f) else Color(0xFF10B981).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Std ${student.standard}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (student.standard == 12) Color(0xFF818CF8) else Color(0xFF34D399),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Progress Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricPill(label = "Level", value = "Lvl ${student.levelNumber}", icon = "⭐", isDark = isDark)
                MetricPill(label = "XP", value = "${student.xp}", icon = "⚡", isDark = isDark)
                MetricPill(label = "Accuracy", value = "${student.accuracy.toInt()}%", icon = "🎯", isDark = isDark)
                MetricPill(label = "Solved", value = "${student.totalCorrect}/${student.totalSolved}", icon = "📝", isDark = isDark)
                MetricPill(label = "Mock Best", value = "${student.mockTestBestScore}/100", icon = "🏆", isDark = isDark)
            }

            Spacer(Modifier.height(10.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { (student.accuracy / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when {
                    student.accuracy >= 85f -> Color(0xFF10B981)
                    student.accuracy >= 75f -> Color(0xFF3B82F6)
                    else -> Color(0xFFF59E0B)
                },
                trackColor = if (isDark) QuestNavySurface else LightSpectrumBorder
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weak: ${student.weakTopics}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) QuestErrorRed else Color(0xFFDC2626),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Details →",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                )
            }
        }
    }
}

@Composable
private fun MetricPill(label: String, value: String, icon: String, isDark: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 11.sp)
            Spacer(Modifier.width(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isDark) QuestTextPrimary else LightSpectrumTextPrimary
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = if (isDark) QuestTextSecondary else LightSpectrumTextSecondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentManagementSection(
    state: AdminDashboardUiState,
    viewModel: AdminDashboardViewModel,
    isDark: Boolean,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    val allChapters = remember { MathematicsRepositoryCatalog.allChapters }
    var chapterDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Notice Banner
        Surface(
            color = Color(0xFF6366F1).copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🛡️", fontSize = 24.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "ADMIN CONTENT AUTHORING ENGINE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF818CF8)
                    )
                    Text(
                        text = "Added items are immediately saved to SQLite database and live in student quizzes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = textSecondary
                    )
                }
            }
        }

        // Sub-tabs (Add Question, Add Formula, Manage Content)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.cmsSubTab == 0,
                onClick = { viewModel.setCmsSubTab(0) },
                label = { Text("Add Question") },
                leadingIcon = { Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (isDark) QuestPrimaryBlue else LightSpectrumActionPrimary,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = state.cmsSubTab == 1,
                onClick = { viewModel.setCmsSubTab(1) },
                label = { Text("Add Formula") },
                leadingIcon = { Icon(Icons.Default.Functions, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (isDark) QuestPrimaryBlue else LightSpectrumActionPrimary,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = state.cmsSubTab == 2,
                onClick = { viewModel.setCmsSubTab(2) },
                label = { Text("Inventory (${state.totalQuestionsCount} Qs)") },
                leadingIcon = { Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (isDark) QuestPrimaryBlue else LightSpectrumActionPrimary,
                    selectedLabelColor = Color.White
                )
            )
        }

        when (state.cmsSubTab) {
            0 -> {
                // ================= ADD QUESTION FORM =================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "1. Class & Syllabus Mapping",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )

                        // Standard Selector (11 vs 12)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = state.qStdClass == 12,
                                onClick = { viewModel.updateQuestionField(stdClass = 12) },
                                label = { Text("Class 12 (Std XII)") }
                            )
                            FilterChip(
                                selected = state.qStdClass == 11,
                                onClick = { viewModel.updateQuestionField(stdClass = 11) },
                                label = { Text("Class 11 (Std XI)") }
                            )
                        }

                        // Chapter Selection Exposed Dropdown
                        ExposedDropdownMenuBox(
                            expanded = chapterDropdownExpanded,
                            onExpandedChange = { chapterDropdownExpanded = !chapterDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = state.qChapter,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Target Chapter") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = chapterDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("admin_chapter_selector"),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = chapterDropdownExpanded,
                                onDismissRequest = { chapterDropdownExpanded = false }
                            ) {
                                val filteredChapters = allChapters.filter { it.standard == state.qStdClass }
                                filteredChapters.forEach { ch ->
                                    DropdownMenuItem(
                                        text = { Text(ch.name) },
                                        onClick = {
                                            viewModel.updateQuestionField(chapter = ch.name)
                                            chapterDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = state.qTopic,
                                onValueChange = { viewModel.updateQuestionField(topic = it) },
                                label = { Text("Topic") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = state.qSubtopic,
                                onValueChange = { viewModel.updateQuestionField(subtopic = it) },
                                label = { Text("Subtopic") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = state.qIsPyq,
                                    onCheckedChange = { viewModel.updateQuestionField(isPyq = it) }
                                )
                                Text("MHT-CET PYQ", style = MaterialTheme.typography.bodyMedium, color = textPrimary)
                            }
                            if (state.qIsPyq) {
                                OutlinedTextField(
                                    value = state.qPyqYear,
                                    onValueChange = { viewModel.updateQuestionField(pyqYear = it) },
                                    label = { Text("Exam Year") },
                                    modifier = Modifier.width(140.dp),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = borderColor)

                        Text(
                            text = "2. Question Statement & Math",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )

                        OutlinedTextField(
                            value = state.qText,
                            onValueChange = { viewModel.updateQuestionField(text = it) },
                            placeholder = { Text("Enter question statement. Supports LaTeX e.g. \\int x^2 dx, \\frac{a}{b}...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp)
                                .testTag("admin_question_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Live Math Preview Box
                        if (state.qText.isNotBlank()) {
                            Text(
                                text = "Live Question Preview:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = textSecondary
                            )
                            MathFormulaRenderer(rawFormula = state.qText, isDark = isDark)
                        }

                        HorizontalDivider(color = borderColor)

                        Text(
                            text = "3. Multiple Choice Options & Answer",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )

                        OutlinedTextField(
                            value = state.qOptionA,
                            onValueChange = { viewModel.updateQuestionField(optionA = it) },
                            label = { Text("Option A") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = state.qOptionB,
                            onValueChange = { viewModel.updateQuestionField(optionB = it) },
                            label = { Text("Option B") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = state.qOptionC,
                            onValueChange = { viewModel.updateQuestionField(optionC = it) },
                            label = { Text("Option C") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = state.qOptionD,
                            onValueChange = { viewModel.updateQuestionField(optionD = it) },
                            label = { Text("Option D") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Text(
                            text = "Select Correct Answer:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("A", "B", "C", "D").forEach { opt ->
                                val isSelected = state.qCorrectAnswer == opt
                                Surface(
                                    color = if (isSelected) Color(0xFF10B981) else if (isDark) QuestNavySurface else LightSpectrumSurface,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF10B981) else borderColor
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.updateQuestionField(correctAnswer = opt) }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Option $opt",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (isSelected) Color.White else textPrimary
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = borderColor)

                        Text(
                            text = "4. Detailed Explanation & Teach Me",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )

                        OutlinedTextField(
                            value = state.qExplanation,
                            onValueChange = { viewModel.updateQuestionField(explanation = it) },
                            placeholder = { Text("Step by step explanation of the solution...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 80.dp),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = state.qFormula,
                                onValueChange = { viewModel.updateQuestionField(formula = it) },
                                label = { Text("Formula Used") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = state.qShortcut,
                                onValueChange = { viewModel.updateQuestionField(shortcut = it) },
                                label = { Text("Shortcut Trick") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = { viewModel.publishQuestion() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("publish_question_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) QuestSuccessGreen else Color(0xFF059669)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !state.isPublishing
                        ) {
                            if (state.isPublishing) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Icon(Icons.Default.CloudUpload, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "PUBLISH QUESTION TO LIVE SYLLABUS",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            1 -> {
                // ================= ADD FORMULA FORM =================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Add New Mathematical Formula to Vault",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = state.fStdClass == 12,
                                onClick = { viewModel.updateFormulaField(stdClass = 12) },
                                label = { Text("Class 12") }
                            )
                            FilterChip(
                                selected = state.fStdClass == 11,
                                onClick = { viewModel.updateFormulaField(stdClass = 11) },
                                label = { Text("Class 11") }
                            )
                        }

                        OutlinedTextField(
                            value = state.fChapter,
                            onValueChange = { viewModel.updateFormulaField(chapter = it) },
                            label = { Text("Chapter") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = state.fTopic,
                            onValueChange = { viewModel.updateFormulaField(topic = it) },
                            label = { Text("Topic") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = state.fTitle,
                            onValueChange = { viewModel.updateFormulaField(title = it) },
                            label = { Text("Formula Title (e.g. Distance between Skew Lines)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_formula_title_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = state.fFormula,
                            onValueChange = { viewModel.updateFormulaField(formula = it) },
                            label = { Text("Formula LaTeX / Expression") },
                            placeholder = { Text("e.g. d = \\frac{|(\\vec{a}_2 - \\vec{a}_1) \\cdot (\\vec{b}_1 \\times \\vec{b}_2)|}{|\\vec{b}_1 \\times \\vec{b}_2|}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 90.dp)
                                .testTag("admin_formula_expression_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        if (state.fFormula.isNotBlank()) {
                            Text(
                                text = "Formula Live Preview:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = textSecondary
                            )
                            MathFormulaRenderer(rawFormula = state.fFormula, isDark = isDark)
                        }

                        OutlinedTextField(
                            value = state.fVariablesMeaning,
                            onValueChange = { viewModel.updateFormulaField(variablesMeaning = it) },
                            label = { Text("Variables Meaning") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = state.fWhenToUse,
                            onValueChange = { viewModel.updateFormulaField(whenToUse = it) },
                            label = { Text("When to Use & Conditions") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = state.fShortcut,
                            onValueChange = { viewModel.updateFormulaField(shortcut = it) },
                            label = { Text("Exam Shortcut / Hack") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Button(
                            onClick = { viewModel.publishFormula() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("publish_formula_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) QuestPrimaryBlue else LightSpectrumActionPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !state.isPublishing
                        ) {
                            if (state.isPublishing) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "SAVE FORMULA TO VAULT",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // ================= CONTENT INVENTORY =================
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Recently Added Questions (${state.recentQuestions.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = textPrimary
                    )

                    state.recentQuestions.take(15).forEach { q ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${q.chapter} • Std ${q.stdClass}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = q.question,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = textPrimary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "Ans: (${q.correctAnswer}) | ${q.difficulty}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = textSecondary
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.deleteQuestion(q.id) }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = QuestErrorRed)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Recently Added Formulas (${state.recentFormulas.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = textPrimary
                    )

                    state.recentFormulas.take(10).forEach { f ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${f.title} (${f.chapter})",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = textPrimary
                                    )
                                    Text(
                                        text = f.formula,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.deleteFormula(f.id) }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = QuestErrorRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CohortAnalyticsSection(
    state: AdminDashboardUiState,
    isDark: Boolean,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    val students = state.students
    val std12Count = students.count { it.standard == 12 }
    val std11Count = students.count { it.standard == 11 }
    val avgScoreMocks = if (students.isNotEmpty()) students.map { it.mockTestAvgScore }.average().toInt() else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cohort Distribution & Exam Readiness",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = textPrimary
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Class 12 (Core Syllabus)", style = MaterialTheme.typography.bodyMedium, color = textSecondary)
                    Text("$std12Count Students", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = textPrimary)
                }
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { if (students.isNotEmpty()) std12Count.toFloat() / students.size else 0f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF6366F1),
                    trackColor = if (isDark) QuestNavySurface else LightSpectrumBorder
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Class 11 (Foundational Topics)", style = MaterialTheme.typography.bodyMedium, color = textSecondary)
                    Text("$std11Count Students", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = textPrimary)
                }
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { if (students.isNotEmpty()) std11Count.toFloat() / students.size else 0f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF10B981),
                    trackColor = if (isDark) QuestNavySurface else LightSpectrumBorder
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Average Mock Score (Paper 1):", style = MaterialTheme.typography.bodyMedium, color = textSecondary)
                    Text("$avgScoreMocks / 100", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFFF59E0B))
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Most Common High-Yield Error Patterns",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = textPrimary
                )
                Spacer(Modifier.height(10.dp))

                ErrorPatternRow("Calculation & Sign Mistakes", 38, Color(0xFFEF4444))
                ErrorPatternRow("Formula Recall under Time Pressure", 28, Color(0xFFF59E0B))
                ErrorPatternRow("Concept Misapplication in Conics & Integrals", 22, Color(0xFF3B82F6))
                ErrorPatternRow("Silly Option Reading Slip-ups", 12, Color(0xFF10B981))
            }
        }
    }
}

@Composable
private fun ErrorPatternRow(label: String, percentage: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = QuestTextPrimary)
            Text("$percentage%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = color)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = QuestNavySurface
        )
    }
}

@Composable
private fun StudentDetailDialog(
    student: StudentEntity,
    isDark: Boolean,
    onDismiss: () -> Unit,
    onUpdateNotes: (String) -> Unit,
    onAwardXp: (Int) -> Unit
) {
    var editableNotes by remember { mutableStateOf(student.notes) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDark) QuestNavyCard else LightSpectrumCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) QuestNavyBorder else LightSpectrumBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = student.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) QuestTextPrimary else LightSpectrumTextPrimary
                        )
                        Text(
                            text = "${student.email} • ${student.targetExam}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) QuestTextSecondary else LightSpectrumTextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(color = if (isDark) QuestNavyBorder else LightSpectrumBorder)

                // Key Performance Highlights
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Rank Title", style = MaterialTheme.typography.labelSmall, color = QuestTextSecondary)
                        Text(student.levelTitle, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFFF59E0B))
                    }
                    Column {
                        Text("Day Streak", style = MaterialTheme.typography.labelSmall, color = QuestTextSecondary)
                        Text("${student.streakDays} Days 🔥", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = QuestErrorRed)
                    }
                    Column {
                        Text("Study Time", style = MaterialTheme.typography.labelSmall, color = QuestTextSecondary)
                        Text("${student.studyTimeMinutes} min", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = QuestPrimaryBlueLight)
                    }
                }

                // Mock Test Performance Breakdown
                Surface(
                    color = if (isDark) QuestNavySurface else LightSpectrumSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "MOCK TEST RECORD (50 Qs MHT-CET)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tests Taken: ${student.mockTestsTaken}", style = MaterialTheme.typography.bodySmall)
                            Text("Highest: ${student.mockTestBestScore}/100", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF10B981))
                            Text("Average: ${student.mockTestAvgScore}/100", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                // Strengths & Weaknesses
                Text(
                    text = "Strong Areas: ${student.strongTopics}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF10B981)
                )
                Text(
                    text = "Weak Topics: ${student.weakTopics}",
                    style = MaterialTheme.typography.bodySmall,
                    color = QuestErrorRed
                )

                // Admin Remarks & Notes
                OutlinedTextField(
                    value = editableNotes,
                    onValueChange = { editableNotes = it },
                    label = { Text("Admin Remarks & Guidance Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onAwardXp(100) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("+100 XP Bonus")
                    }
                    Button(
                        onClick = { onUpdateNotes(editableNotes) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Remarks")
                    }
                }
            }
        }
    }
}
