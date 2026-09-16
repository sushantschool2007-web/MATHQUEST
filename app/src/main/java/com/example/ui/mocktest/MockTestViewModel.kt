package com.example.ui.mocktest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.MockTestResultEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.repository.MathRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class QuestionStatus {
    NOT_VISITED,
    NOT_ANSWERED,
    ANSWERED,
    MARKED_FOR_REVIEW,
    ANSWERED_AND_MARKED
}

data class MockQuestionState(
    val question: QuestionEntity,
    val selectedOption: String? = null,
    val status: QuestionStatus = QuestionStatus.NOT_VISITED
)

data class MockTestUiState(
    val questions: List<MockQuestionState> = emptyList(),
    val currentIndex: Int = 0,
    val remainingSeconds: Int = 90 * 60, // 90 minutes = 5400 seconds
    val totalDurationSeconds: Int = 90 * 60,
    val isTimerRunning: Boolean = true,
    val showPalette: Boolean = false,
    val showSubmitDialog: Boolean = false,
    val showExitConfirmDialog: Boolean = false,
    val isSubmitted: Boolean = false,
    val savedResultId: Long? = null,
    val testResult: MockTestResultEntity? = null
)

class MockTestViewModel(private val repository: MathRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MockTestUiState())
    val uiState: StateFlow<MockTestUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadMockQuestions()
        startTimer()
    }

    private fun loadMockQuestions() {
        viewModelScope.launch {
            val list = repository.generateMockTestQuestions()
            val questionStates = list.mapIndexed { index, q ->
                MockQuestionState(
                    question = q,
                    status = if (index == 0) QuestionStatus.NOT_ANSWERED else QuestionStatus.NOT_VISITED
                )
            }
            _uiState.value = _uiState.value.copy(questions = questionStates)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _uiState.value
                if (!current.isTimerRunning || current.isSubmitted) break
                if (current.remainingSeconds <= 1) {
                    _uiState.value = current.copy(remainingSeconds = 0)
                    submitTest()
                    break
                } else {
                    _uiState.value = current.copy(remainingSeconds = current.remainingSeconds - 1)
                }
            }
        }
    }

    fun pauseTimer() {
        _uiState.value = _uiState.value.copy(isTimerRunning = false)
    }

    fun resumeTimer() {
        if (!_uiState.value.isSubmitted) {
            _uiState.value = _uiState.value.copy(isTimerRunning = true)
            startTimer()
        }
    }

    fun selectOption(option: String) {
        val current = _uiState.value
        if (current.questions.isEmpty()) return
        val list = current.questions.toMutableList()
        val item = list[current.currentIndex]

        val newStatus = if (item.status == QuestionStatus.MARKED_FOR_REVIEW || item.status == QuestionStatus.ANSWERED_AND_MARKED) {
            QuestionStatus.ANSWERED_AND_MARKED
        } else {
            QuestionStatus.ANSWERED
        }

        list[current.currentIndex] = item.copy(
            selectedOption = option,
            status = newStatus
        )
        _uiState.value = current.copy(questions = list)
    }

    fun clearResponse() {
        val current = _uiState.value
        if (current.questions.isEmpty()) return
        val list = current.questions.toMutableList()
        val item = list[current.currentIndex]

        val newStatus = if (item.status == QuestionStatus.ANSWERED_AND_MARKED || item.status == QuestionStatus.MARKED_FOR_REVIEW) {
            QuestionStatus.MARKED_FOR_REVIEW
        } else {
            QuestionStatus.NOT_ANSWERED
        }

        list[current.currentIndex] = item.copy(
            selectedOption = null,
            status = newStatus
        )
        _uiState.value = current.copy(questions = list)
    }

    fun markForReview() {
        val current = _uiState.value
        if (current.questions.isEmpty()) return
        val list = current.questions.toMutableList()
        val item = list[current.currentIndex]

        val newStatus = if (item.selectedOption != null) {
            QuestionStatus.ANSWERED_AND_MARKED
        } else {
            QuestionStatus.MARKED_FOR_REVIEW
        }

        list[current.currentIndex] = item.copy(status = newStatus)

        val nextIndex = (current.currentIndex + 1).coerceAtMost(list.size - 1)
        if (list[nextIndex].status == QuestionStatus.NOT_VISITED) {
            list[nextIndex] = list[nextIndex].copy(status = QuestionStatus.NOT_ANSWERED)
        }

        _uiState.value = current.copy(questions = list, currentIndex = nextIndex)
    }

    fun toggleMarkForReview() {
        val current = _uiState.value
        if (current.questions.isEmpty()) return
        val list = current.questions.toMutableList()
        val item = list[current.currentIndex]

        val newStatus = when (item.status) {
            QuestionStatus.MARKED_FOR_REVIEW -> QuestionStatus.NOT_ANSWERED
            QuestionStatus.ANSWERED_AND_MARKED -> QuestionStatus.ANSWERED
            QuestionStatus.ANSWERED -> QuestionStatus.ANSWERED_AND_MARKED
            QuestionStatus.NOT_ANSWERED, QuestionStatus.NOT_VISITED -> {
                if (item.selectedOption != null) QuestionStatus.ANSWERED_AND_MARKED else QuestionStatus.MARKED_FOR_REVIEW
            }
        }

        list[current.currentIndex] = item.copy(status = newStatus)
        _uiState.value = current.copy(questions = list)
    }

    fun saveAndNext() {
        val current = _uiState.value
        if (current.questions.isEmpty()) return
        val list = current.questions.toMutableList()
        val item = list[current.currentIndex]

        if (item.selectedOption != null && item.status == QuestionStatus.NOT_ANSWERED) {
            list[current.currentIndex] = item.copy(status = QuestionStatus.ANSWERED)
        }

        val nextIndex = (current.currentIndex + 1).coerceAtMost(list.size - 1)
        if (list[nextIndex].status == QuestionStatus.NOT_VISITED) {
            list[nextIndex] = list[nextIndex].copy(status = QuestionStatus.NOT_ANSWERED)
        }

        _uiState.value = current.copy(questions = list, currentIndex = nextIndex)
    }

    fun previousQuestion() {
        val current = _uiState.value
        if (current.questions.isEmpty() || current.currentIndex <= 0) return
        val list = current.questions.toMutableList()
        val prevIndex = current.currentIndex - 1

        if (list[prevIndex].status == QuestionStatus.NOT_VISITED) {
            list[prevIndex] = list[prevIndex].copy(status = QuestionStatus.NOT_ANSWERED)
        }

        _uiState.value = current.copy(questions = list, currentIndex = prevIndex)
    }

    fun navigateToQuestion(index: Int) {
        val current = _uiState.value
        if (current.questions.isEmpty() || index !in current.questions.indices) return
        val list = current.questions.toMutableList()
        if (list[index].status == QuestionStatus.NOT_VISITED) {
            list[index] = list[index].copy(status = QuestionStatus.NOT_ANSWERED)
        }
        _uiState.value = current.copy(
            questions = list,
            currentIndex = index,
            showPalette = false
        )
    }

    fun togglePalette(show: Boolean) {
        _uiState.value = _uiState.value.copy(showPalette = show)
    }

    fun showSubmitConfirmation() {
        _uiState.value = _uiState.value.copy(showSubmitDialog = true)
    }

    fun dismissSubmitConfirmation() {
        _uiState.value = _uiState.value.copy(showSubmitDialog = false)
    }

    fun showExitConfirmation() {
        _uiState.value = _uiState.value.copy(showExitConfirmDialog = true)
    }

    fun dismissExitConfirmation() {
        _uiState.value = _uiState.value.copy(showExitConfirmDialog = false)
    }

    fun submitTest(onFinished: ((MockTestResultEntity) -> Unit)? = null) {
        val current = _uiState.value
        if (current.isSubmitted) return

        timerJob?.cancel()

        var correct = 0
        var incorrect = 0
        var unattempted = 0
        var xiScore = 0
        var xiiScore = 0

        current.questions.forEach { item ->
            val isAns = item.selectedOption != null
            if (!isAns) {
                unattempted++
            } else {
                if (item.selectedOption == item.question.correctAnswer) {
                    correct++
                    if (item.question.stdClass == 11) xiScore += 2 else xiiScore += 2
                } else {
                    incorrect++
                }
            }
        }

        val totalScore = (correct * 2) // 2 marks each
        val totalQ = current.questions.size.coerceAtLeast(1)
        val accuracy = if (correct + incorrect > 0) (correct.toFloat() / (correct + incorrect).toFloat()) * 100f else 0f
        val percentage = (totalScore.toFloat() / (totalQ * 2).toFloat()) * 100f
        val timeSpent = (90 * 60) - current.remainingSeconds

        val resultEntity = MockTestResultEntity(
            score = totalScore,
            percentage = percentage,
            accuracy = accuracy,
            correctCount = correct,
            incorrectCount = incorrect,
            unattemptedCount = unattempted,
            totalQuestions = totalQ,
            classXiScore = xiScore,
            classXiiScore = xiiScore,
            timeTakenSeconds = timeSpent,
            fastestQuestionTimeSeconds = 45,
            slowestQuestionTimeSeconds = 135,
            strongTopics = "Calculus, Logic",
            weakTopics = "Trigonometry",
            recommendedRevision = "Focus on XI Trigonometry & Vectors",
            prepLevelEstimate = if (totalScore >= 70) "Advanced" else "Developing"
        )

        viewModelScope.launch {
            val id = repository.saveMockTestResult(resultEntity)
            val finalResult = resultEntity.copy(id = id)
            _uiState.value = current.copy(
                isSubmitted = true,
                showSubmitDialog = false,
                showExitConfirmDialog = false,
                savedResultId = id,
                testResult = finalResult
            )
            onFinished?.invoke(finalResult)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
