package com.example.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.StudentEntity
import com.example.data.local.entity.UserLoginHistoryEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.MathematicsRepositoryCatalog
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AdminDashboardUiState(
    val activeTab: Int = 0, // 0: Students, 1: User Logins, 2: Add Content, 3: Cohort Analytics
    val cmsSubTab: Int = 0, // 0: Add Question, 1: Add Formula, 2: Content Inventory
    val students: List<StudentEntity> = emptyList(),
    val filteredStudents: List<StudentEntity> = emptyList(),
    val searchQuery: String = "",
    val classFilter: String = "All",
    val selectedStudent: StudentEntity? = null,
    val loginHistory: List<UserLoginHistoryEntity> = emptyList(),
    val filteredLoginHistory: List<UserLoginHistoryEntity> = emptyList(),
    val loginSearchQuery: String = "",
    val loginMethodFilter: String = "All", // "All", "Email & Password", "Registration", "Guest"
    val userProfiles: List<UserProfileEntity> = emptyList(),
    val selectedLogin: UserLoginHistoryEntity? = null,
    val totalQuestionsCount: Int = 0,
    val totalFormulasCount: Int = 0,
    val recentQuestions: List<QuestionEntity> = emptyList(),
    val recentFormulas: List<FormulaEntity> = emptyList(),
    val snackbarMessage: String? = null,
    val isPublishing: Boolean = false,
    // Add Question Form State
    val qStdClass: Int = 12,
    val qChapter: String = "Matrices",
    val qTopic: String = "Inverse of Matrix",
    val qSubtopic: String = "Adjoint Method",
    val qDifficulty: String = "LEVEL 4 — CET Medium",
    val qIsPyq: Boolean = true,
    val qPyqYear: String = "MHT-CET 2024",
    val qText: String = "",
    val qOptionA: String = "",
    val qOptionB: String = "",
    val qOptionC: String = "",
    val qOptionD: String = "",
    val qCorrectAnswer: String = "A",
    val qExplanation: String = "",
    val qFormula: String = "",
    val qShortcut: String = "",
    val qHint1: String = "",
    val qHint2: String = "",
    val qHint3: String = "",
    val qTeachMeGiven: String = "",
    val qTeachMeWeNeed: String = "",
    val qTeachMeFormula: String = "",
    val qTeachMeSubstitution: String = "",
    val qTeachMeCalculation: String = "",
    val qTeachMeFinalAnswer: String = "",
    val qTeachMeShortcut: String = "",
    val qEstimatedTime: Int = 45,
    val qXp: Int = 20,
    // Add Formula Form State
    val fStdClass: Int = 12,
    val fChapter: String = "Vectors",
    val fTopic: String = "Cross Product",
    val fTitle: String = "",
    val fFormula: String = "",
    val fVariablesMeaning: String = "",
    val fWhenToUse: String = "",
    val fExample: String = "",
    val fShortcut: String = ""
)

class AdminDashboardViewModel(
    private val repository: MathRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialStudentsIfEmpty()
            repository.seedInitialLoginsIfEmpty()
        }

        // Sync current active student with userPreferences
        viewModelScope.launch {
            repository.userPreferencesFlow.collectLatest { prefs ->
                repository.syncCurrentStudentProgress(prefs)
            }
        }

        // Collect students
        viewModelScope.launch {
            repository.allStudents.collectLatest { list ->
                _uiState.update { current ->
                    current.copy(
                        students = list,
                        filteredStudents = filterStudents(list, current.searchQuery, current.classFilter)
                    )
                }
            }
        }

        // Collect user login events and profiles
        viewModelScope.launch {
            repository.allLoginHistory.collectLatest { list ->
                _uiState.update { current ->
                    current.copy(
                        loginHistory = list,
                        filteredLoginHistory = filterLogins(list, current.loginSearchQuery, current.loginMethodFilter)
                    )
                }
            }
        }

        viewModelScope.launch {
            repository.allUserProfiles.collectLatest { profiles ->
                _uiState.update { it.copy(userProfiles = profiles) }
            }
        }

        // Collect recent content
        viewModelScope.launch {
            repository.getRecentQuestions(30).collectLatest { qList ->
                _uiState.update { it.copy(recentQuestions = qList) }
            }
        }

        viewModelScope.launch {
            repository.getRecentFormulas(30).collectLatest { fList ->
                _uiState.update { it.copy(recentFormulas = fList) }
            }
        }

        // Refresh counts
        viewModelScope.launch {
            refreshCounts()
        }
    }

    private suspend fun refreshCounts() {
        val qCount = repository.getQuestionCount()
        val fCount = repository.getFormulaCount()
        _uiState.update { it.copy(totalQuestionsCount = qCount, totalFormulasCount = fCount) }
    }

    fun setActiveTab(tab: Int) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun setCmsSubTab(subTab: Int) {
        _uiState.update { it.copy(cmsSubTab = subTab) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { current ->
            current.copy(
                searchQuery = query,
                filteredStudents = filterStudents(current.students, query, current.classFilter)
            )
        }
    }

    fun setClassFilter(filter: String) {
        _uiState.update { current ->
            current.copy(
                classFilter = filter,
                filteredStudents = filterStudents(current.students, current.searchQuery, filter)
            )
        }
    }

    fun selectStudent(student: StudentEntity?) {
        _uiState.update { it.copy(selectedStudent = student) }
    }

    private fun filterStudents(list: List<StudentEntity>, query: String, filter: String): List<StudentEntity> {
        return list.filter { s ->
            val matchesQuery = query.isBlank() ||
                    s.name.contains(query, ignoreCase = true) ||
                    s.email.contains(query, ignoreCase = true) ||
                    s.targetExam.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "Class 12" -> s.standard == 12
                "Class 11" -> s.standard == 11
                "Top Performers" -> s.accuracy >= 85f
                "Needs Support" -> s.accuracy < 75f
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    fun setLoginSearchQuery(query: String) {
        _uiState.update { current ->
            current.copy(
                loginSearchQuery = query,
                filteredLoginHistory = filterLogins(current.loginHistory, query, current.loginMethodFilter)
            )
        }
    }

    fun setLoginMethodFilter(filter: String) {
        _uiState.update { current ->
            current.copy(
                loginMethodFilter = filter,
                filteredLoginHistory = filterLogins(current.loginHistory, current.loginSearchQuery, filter)
            )
        }
    }

    fun selectLogin(login: UserLoginHistoryEntity?) {
        _uiState.update { it.copy(selectedLogin = login) }
    }

    private fun filterLogins(list: List<UserLoginHistoryEntity>, query: String, filter: String): List<UserLoginHistoryEntity> {
        return list.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.email.contains(query, ignoreCase = true) ||
                    item.displayName.contains(query, ignoreCase = true) ||
                    item.deviceModel.contains(query, ignoreCase = true) ||
                    item.loginMethod.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "Email & Password" -> item.loginMethod.contains("Email", ignoreCase = true)
                "Registration" -> item.loginMethod.contains("Registration", ignoreCase = true)
                "Guest" -> item.loginMethod.contains("Guest", ignoreCase = true)
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    fun recordSimulatedLogin(name: String, email: String, method: String = "Email & Password") {
        viewModelScope.launch {
            repository.recordLogin(
                uid = "usr_${System.currentTimeMillis() % 100000}",
                email = email.trim(),
                displayName = name.trim(),
                loginMethod = method
            )
            _uiState.update { it.copy(snackbarMessage = "Logged in: $name ($email)") }
        }
    }

    fun deleteLogin(id: Long) {
        viewModelScope.launch {
            repository.deleteLoginRecord(id)
            _uiState.update { it.copy(snackbarMessage = "Login record removed") }
        }
    }

    fun clearAllLogins() {
        viewModelScope.launch {
            repository.clearLoginHistory()
            _uiState.update { it.copy(snackbarMessage = "Login history cleared") }
        }
    }

    // Question form state updates
    fun updateQuestionField(
        stdClass: Int? = null,
        chapter: String? = null,
        topic: String? = null,
        subtopic: String? = null,
        difficulty: String? = null,
        isPyq: Boolean? = null,
        pyqYear: String? = null,
        text: String? = null,
        optionA: String? = null,
        optionB: String? = null,
        optionC: String? = null,
        optionD: String? = null,
        correctAnswer: String? = null,
        explanation: String? = null,
        formula: String? = null,
        shortcut: String? = null,
        hint1: String? = null,
        hint2: String? = null,
        hint3: String? = null,
        given: String? = null,
        weNeed: String? = null,
        tmFormula: String? = null,
        substitution: String? = null,
        calculation: String? = null,
        finalAnswer: String? = null,
        tmShortcut: String? = null,
        estTime: Int? = null,
        xp: Int? = null
    ) {
        _uiState.update { current ->
            current.copy(
                qStdClass = stdClass ?: current.qStdClass,
                qChapter = chapter ?: current.qChapter,
                qTopic = topic ?: current.qTopic,
                qSubtopic = subtopic ?: current.qSubtopic,
                qDifficulty = difficulty ?: current.qDifficulty,
                qIsPyq = isPyq ?: current.qIsPyq,
                qPyqYear = pyqYear ?: current.qPyqYear,
                qText = text ?: current.qText,
                qOptionA = optionA ?: current.qOptionA,
                qOptionB = optionB ?: current.qOptionB,
                qOptionC = optionC ?: current.qOptionC,
                qOptionD = optionD ?: current.qOptionD,
                qCorrectAnswer = correctAnswer ?: current.qCorrectAnswer,
                qExplanation = explanation ?: current.qExplanation,
                qFormula = formula ?: current.qFormula,
                qShortcut = shortcut ?: current.qShortcut,
                qHint1 = hint1 ?: current.qHint1,
                qHint2 = hint2 ?: current.qHint2,
                qHint3 = hint3 ?: current.qHint3,
                qTeachMeGiven = given ?: current.qTeachMeGiven,
                qTeachMeWeNeed = weNeed ?: current.qTeachMeWeNeed,
                qTeachMeFormula = tmFormula ?: current.qTeachMeFormula,
                qTeachMeSubstitution = substitution ?: current.qTeachMeSubstitution,
                qTeachMeCalculation = calculation ?: current.qTeachMeCalculation,
                qTeachMeFinalAnswer = finalAnswer ?: current.qTeachMeFinalAnswer,
                qTeachMeShortcut = tmShortcut ?: current.qTeachMeShortcut,
                qEstimatedTime = estTime ?: current.qEstimatedTime,
                qXp = xp ?: current.qXp
            )
        }
    }

    // Formula form state updates
    fun updateFormulaField(
        stdClass: Int? = null,
        chapter: String? = null,
        topic: String? = null,
        title: String? = null,
        formula: String? = null,
        variablesMeaning: String? = null,
        whenToUse: String? = null,
        example: String? = null,
        shortcut: String? = null
    ) {
        _uiState.update { current ->
            current.copy(
                fStdClass = stdClass ?: current.fStdClass,
                fChapter = chapter ?: current.fChapter,
                fTopic = topic ?: current.fTopic,
                fTitle = title ?: current.fTitle,
                fFormula = formula ?: current.fFormula,
                fVariablesMeaning = variablesMeaning ?: current.fVariablesMeaning,
                fWhenToUse = whenToUse ?: current.fWhenToUse,
                fExample = example ?: current.fExample,
                fShortcut = shortcut ?: current.fShortcut
            )
        }
    }

    fun publishQuestion() {
        val state = _uiState.value
        if (state.qText.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Question text cannot be empty!") }
            return
        }
        if (state.qOptionA.isBlank() || state.qOptionB.isBlank() || state.qOptionC.isBlank() || state.qOptionD.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please provide all 4 options (A, B, C, D)!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isPublishing = true) }
            val newQuestion = QuestionEntity(
                stdClass = state.qStdClass,
                chapter = state.qChapter,
                topic = state.qTopic.ifBlank { state.qChapter },
                subtopic = state.qSubtopic.ifBlank { "General" },
                difficulty = state.qDifficulty,
                question = state.qText,
                optionA = state.qOptionA,
                optionB = state.qOptionB,
                optionC = state.qOptionC,
                optionD = state.qOptionD,
                correctAnswer = state.qCorrectAnswer,
                explanation = state.qExplanation.ifBlank { "Correct option is (${state.qCorrectAnswer})." },
                formula = state.qFormula,
                shortcut = state.qShortcut,
                hint1 = state.qHint1.ifBlank { "Recall standard identities and theorems." },
                hint2 = state.qHint2.ifBlank { "Check substitution and algebraic signs." },
                hint3 = state.qHint3.ifBlank { "Substitute options or simplify expression." },
                estimatedTime = state.qEstimatedTime,
                xp = state.qXp,
                tags = "${state.qChapter}, ${if (state.qStdClass == 11) "Std XI" else "Std XII"}, Admin Created",
                isPyq = state.qIsPyq,
                pyqYear = if (state.qIsPyq) state.qPyqYear else "",
                teachMeGiven = state.qTeachMeGiven.ifBlank { state.qText.take(60) },
                teachMeWeNeed = state.qTeachMeWeNeed.ifBlank { "Correct mathematical evaluation" },
                teachMeFormula = state.qTeachMeFormula.ifBlank { state.qFormula },
                teachMeSubstitution = state.qTeachMeSubstitution.ifBlank { "Substitute standard definitions." },
                teachMeCalculation = state.qTeachMeCalculation.ifBlank { state.qExplanation },
                teachMeFinalAnswer = state.qTeachMeFinalAnswer.ifBlank { "Option ${state.qCorrectAnswer}" },
                teachMeShortcut = state.qTeachMeShortcut.ifBlank { state.qShortcut }
            )

            val id = repository.addQuestion(newQuestion)
            refreshCounts()
            _uiState.update {
                it.copy(
                    isPublishing = false,
                    snackbarMessage = "✓ Question #$id added to ${state.qChapter} successfully!",
                    qText = "",
                    qOptionA = "",
                    qOptionB = "",
                    qOptionC = "",
                    qOptionD = "",
                    qExplanation = "",
                    qFormula = "",
                    qShortcut = ""
                )
            }
        }
    }

    fun publishFormula() {
        val state = _uiState.value
        if (state.fTitle.isBlank() || state.fFormula.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Formula Title and Formula Expression are required!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isPublishing = true) }
            val newFormula = FormulaEntity(
                stdClass = state.fStdClass,
                chapter = state.fChapter,
                topic = state.fTopic.ifBlank { state.fChapter },
                title = state.fTitle,
                formula = state.fFormula,
                variablesMeaning = state.fVariablesMeaning.ifBlank { "Standard mathematical notation." },
                whenToUse = state.fWhenToUse.ifBlank { "Standard problems in ${state.fChapter}." },
                example = state.fExample.ifBlank { "Direct substitution." },
                shortcut = state.fShortcut,
                isBookmarked = false
            )

            val id = repository.addFormula(newFormula)
            refreshCounts()
            _uiState.update {
                it.copy(
                    isPublishing = false,
                    snackbarMessage = "✓ Formula #$id added to Formula Vault for ${state.fChapter}!",
                    fTitle = "",
                    fFormula = "",
                    fVariablesMeaning = "",
                    fWhenToUse = "",
                    fExample = "",
                    fShortcut = ""
                )
            }
        }
    }

    fun deleteQuestion(id: Long) {
        viewModelScope.launch {
            repository.deleteQuestion(id)
            refreshCounts()
            _uiState.update { it.copy(snackbarMessage = "Question #$id removed from database.") }
        }
    }

    fun deleteFormula(id: Long) {
        viewModelScope.launch {
            repository.deleteFormula(id)
            refreshCounts()
            _uiState.update { it.copy(snackbarMessage = "Formula #$id removed from database.") }
        }
    }

    fun updateStudentNotes(studentId: String, notes: String) {
        viewModelScope.launch {
            val student = repository.getStudentById(studentId) ?: return@launch
            val updated = student.copy(notes = notes)
            repository.updateStudent(updated)
            _uiState.update { current ->
                current.copy(
                    selectedStudent = if (current.selectedStudent?.studentId == studentId) updated else current.selectedStudent,
                    snackbarMessage = "✓ Admin notes updated for ${student.name}!"
                )
            }
        }
    }

    fun awardBonusXp(studentId: String, bonus: Int) {
        viewModelScope.launch {
            val student = repository.getStudentById(studentId) ?: return@launch
            val newXp = student.xp + bonus
            val updated = student.copy(xp = newXp)
            repository.updateStudent(updated)
            _uiState.update { current ->
                current.copy(
                    selectedStudent = if (current.selectedStudent?.studentId == studentId) updated else current.selectedStudent,
                    snackbarMessage = "✓ +$bonus XP awarded to ${student.name}!"
                )
            }
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
