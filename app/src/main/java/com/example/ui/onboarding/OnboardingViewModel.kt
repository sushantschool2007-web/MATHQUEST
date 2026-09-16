package com.example.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.StudentMathLevel
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DiagnosticQuestion(
    val id: Int,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String,
    val topic: String
)

data class OnboardingUiState(
    val step: Int = 1, // 1: Info input, 2: Diagnostic Test, 3: Diagnostic Result
    val studentName: String = "",
    val targetYear: String = "2026",
    val selectedPrepLevel: String = "Developing",
    val dailyGoalMinutes: Int = 30,
    val targetScore: Int = 90,
    // Diagnostic state
    val diagnosticIndex: Int = 0,
    val selectedOption: String? = null,
    val diagnosticScore: Int = 0,
    val calculatedMathLevel: StudentMathLevel = StudentMathLevel.DEVELOPING,
    val recommendedChapter: String = "Trigonometry II"
)

class OnboardingViewModel(private val repository: MathRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    val diagnosticQuestions = listOf(
        DiagnosticQuestion(
            id = 1,
            question = "What is the value of sin²(45°) + cos²(45°)?",
            optionA = "1/2",
            optionB = "1",
            optionC = "√2",
            optionD = "0",
            correctAnswer = "B",
            topic = "Trigonometry Basics"
        ),
        DiagnosticQuestion(
            id = 2,
            question = "The slope of the line perpendicular to 2x - 3y + 5 = 0 is:",
            optionA = "-3/2",
            optionB = "3/2",
            optionC = "-2/3",
            optionD = "2/3",
            correctAnswer = "A",
            topic = "Straight Line"
        ),
        DiagnosticQuestion(
            id = 3,
            question = "If y = x³, then dy/dx at x = 2 is:",
            optionA = "6",
            optionB = "8",
            optionC = "12",
            optionD = "4",
            correctAnswer = "C",
            topic = "Differentiation"
        )
    )

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(studentName = name)
    }

    fun updateTargetYear(year: String) {
        _uiState.value = _uiState.value.copy(targetYear = year)
    }

    fun updatePrepLevel(level: String) {
        _uiState.value = _uiState.value.copy(selectedPrepLevel = level)
    }

    fun updateDailyGoal(minutes: Int) {
        _uiState.value = _uiState.value.copy(dailyGoalMinutes = minutes)
    }

    fun updateTargetScore(score: Int) {
        _uiState.value = _uiState.value.copy(targetScore = score)
    }

    fun proceedToDiagnostic() {
        _uiState.value = _uiState.value.copy(step = 2, diagnosticIndex = 0, diagnosticScore = 0)
    }

    fun selectDiagnosticOption(option: String) {
        _uiState.value = _uiState.value.copy(selectedOption = option)
    }

    fun submitDiagnosticAnswer() {
        val current = _uiState.value
        val q = diagnosticQuestions[current.diagnosticIndex]
        val isCorrect = current.selectedOption == q.correctAnswer
        val newScore = current.diagnosticScore + (if (isCorrect) 1 else 0)

        if (current.diagnosticIndex < diagnosticQuestions.size - 1) {
            _uiState.value = current.copy(
                diagnosticIndex = current.diagnosticIndex + 1,
                selectedOption = null,
                diagnosticScore = newScore
            )
        } else {
            // Determine result
            val level = when (newScore) {
                3 -> StudentMathLevel.ADVANCED
                2 -> StudentMathLevel.INTERMEDIATE
                1 -> StudentMathLevel.DEVELOPING
                else -> StudentMathLevel.BEGINNER
            }
            val recChapter = when (level) {
                StudentMathLevel.ADVANCED -> "Differentiation"
                StudentMathLevel.INTERMEDIATE -> "Mathematical Logic"
                StudentMathLevel.DEVELOPING -> "Trigonometry II"
                StudentMathLevel.BEGINNER -> "Straight Line"
            }
            _uiState.value = current.copy(
                step = 3,
                diagnosticScore = newScore,
                calculatedMathLevel = level,
                recommendedChapter = recChapter
            )
        }
    }

    fun completeOnboarding(onFinished: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            repository.saveOnboarding(
                studentName = if (state.studentName.isBlank()) "Aspirant" else state.studentName,
                targetYear = state.targetYear,
                prepLevel = state.calculatedMathLevel.title,
                dailyGoalMinutes = state.dailyGoalMinutes,
                targetScore = state.targetScore,
                recommendedChapter = state.recommendedChapter
            )
            onFinished()
        }
    }
}
