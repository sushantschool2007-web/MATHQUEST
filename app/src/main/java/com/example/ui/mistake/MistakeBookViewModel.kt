package com.example.ui.mistake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.MistakeEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MistakeItemUiModel(
    val mistake: MistakeEntity,
    val question: QuestionEntity?
)

data class MistakeBookUiState(
    val mistakeItems: List<MistakeItemUiModel> = emptyList(),
    val showResolved: Boolean = false,
    val activeTeachMeQuestion: QuestionEntity? = null
)

class MistakeBookViewModel(private val repository: MathRepository) : ViewModel() {
    private val _showResolved = MutableStateFlow(false)
    private val _teachMeQuestion = MutableStateFlow<QuestionEntity?>(null)

    val uiState: StateFlow<MistakeBookUiState> = combine(
        _showResolved,
        _teachMeQuestion,
        repository.allMistakes
    ) { showResolved, teachMeQ, mistakes ->
        val filtered = if (showResolved) mistakes else mistakes.filter { !it.isResolved }
        val ids = filtered.map { it.questionId }
        val questions = repository.getQuestionsByIds(ids).associateBy { it.id }

        val models = filtered.map { m ->
            MistakeItemUiModel(mistake = m, question = questions[m.questionId])
        }

        MistakeBookUiState(
            mistakeItems = models,
            showResolved = showResolved,
            activeTeachMeQuestion = teachMeQ
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MistakeBookUiState()
    )

    fun toggleShowResolved() {
        _showResolved.value = !_showResolved.value
    }

    fun markResolved(mistakeId: Long) {
        viewModelScope.launch {
            repository.resolveMistake(mistakeId)
        }
    }

    fun openTeachMe(question: QuestionEntity) {
        _teachMeQuestion.value = question
    }

    fun dismissTeachMe() {
        _teachMeQuestion.value = null
    }
}
