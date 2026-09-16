package com.example.ui.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChapterLearningUiState(
    val chapterName: String = "",
    val activeTab: Int = 1, // 0: Concept & Formula, 1: Practice Questions
    val questions: List<QuestionEntity> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOption: String? = null,
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val unlockedHints: Int = 0,
    val showHintDialog: Boolean = false,
    val showTeachMeDialog: Boolean = false,
    val earnedXp: Int = 0,
    val comboStreak: Int = 0,
    val formulas: List<FormulaEntity> = emptyList(),
    val currentDifficulty: String = "LEVEL 3 — CET Easy"
)

class ChapterLearningViewModel(
    private val repository: MathRepository,
    private val chapterName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChapterLearningUiState(chapterName = chapterName))
    val uiState: StateFlow<ChapterLearningUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.setActiveChapter(chapterName)
            val qList = repository.getQuestionsByChapterSync(chapterName)
            val formulaList = repository.searchFormulas(chapterName).first()

            val finalQuestions = if (qList.isEmpty()) {
                repository.getRandomQuestions(5)
            } else {
                qList
            }

            _uiState.value = _uiState.value.copy(
                questions = finalQuestions,
                formulas = formulaList,
                currentDifficulty = finalQuestions.firstOrNull()?.difficulty ?: "LEVEL 3 — CET Easy"
            )
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(activeTab = tabIndex)
    }

    fun selectOption(option: String) {
        if (_uiState.value.isAnswerSubmitted) return
        _uiState.value = _uiState.value.copy(selectedOption = option)
    }

    fun submitAnswer() {
        val current = _uiState.value
        val q = current.questions.getOrNull(current.currentQuestionIndex) ?: return
        val isCorrect = current.selectedOption == q.correctAnswer
        val newCombo = if (isCorrect) current.comboStreak + 1 else 0
        val baseScore = if (isCorrect) q.xp else 0
        val finalXp = maxOf(5, baseScore - (current.unlockedHints * 2))

        _uiState.value = current.copy(
            isAnswerSubmitted = true,
            isCorrect = isCorrect,
            earnedXp = finalXp,
            comboStreak = newCombo
        )

        viewModelScope.launch {
            repository.recordQuestionAttempt(
                question = q,
                selectedOption = current.selectedOption ?: "",
                isCorrect = isCorrect,
                timeSeconds = 45,
                mistakeType = if (!isCorrect) "Concept gap" else ""
            )
        }
    }

    fun nextQuestion() {
        val current = _uiState.value
        if (current.currentQuestionIndex < current.questions.size - 1) {
            val nextIndex = current.currentQuestionIndex + 1
            _uiState.value = current.copy(
                currentQuestionIndex = nextIndex,
                selectedOption = null,
                isAnswerSubmitted = false,
                isCorrect = false,
                unlockedHints = 0,
                showHintDialog = false,
                showTeachMeDialog = false,
                currentDifficulty = current.questions[nextIndex].difficulty
            )
        }
    }

    fun showHint() {
        _uiState.value = _uiState.value.copy(showHintDialog = true)
    }

    fun dismissHint() {
        _uiState.value = _uiState.value.copy(showHintDialog = false)
    }

    fun unlockNextHint() {
        val current = _uiState.value
        if (current.unlockedHints < 3) {
            _uiState.value = current.copy(unlockedHints = current.unlockedHints + 1)
        }
    }

    fun showTeachMe() {
        _uiState.value = _uiState.value.copy(showTeachMeDialog = true)
    }

    fun dismissTeachMe() {
        _uiState.value = _uiState.value.copy(showTeachMeDialog = false)
    }
}
