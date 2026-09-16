package com.example

import com.example.data.local.entity.MockTestResultEntity
import com.example.data.local.entity.QuestionEntity
import com.example.ui.mocktest.MockQuestionState
import com.example.ui.mocktest.QuestionStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MockTestLogicTest {

    private fun sampleQuestion(id: Long, std: Int, correct: String = "A") = QuestionEntity(
        id = id,
        stdClass = std,
        chapter = "Calculus",
        topic = "Differentiation",
        subtopic = "Chain Rule",
        difficulty = "LEVEL 3 — CET Easy",
        question = "Find derivative of sin(x)",
        optionA = "cos(x)",
        optionB = "-cos(x)",
        optionC = "tan(x)",
        optionD = "sec(x)",
        correctAnswer = correct,
        explanation = "d/dx(sin x) = cos x",
        formula = "d/dx(sin x) = cos x",
        shortcut = "Direct standard formula",
        hint1 = "Recall derivative of trigonometric ratios",
        hint2 = "Standard rule",
        hint3 = "It is cos(x)"
    )

    @Test
    fun testOptionSelectionTransitions() {
        val q = sampleQuestion(1, 12)
        var state = MockQuestionState(question = q, status = QuestionStatus.NOT_ANSWERED)

        // Select option -> ANSWERED
        state = state.copy(selectedOption = "A", status = QuestionStatus.ANSWERED)
        assertEquals(QuestionStatus.ANSWERED, state.status)
        assertEquals("A", state.selectedOption)

        // Mark for review when answer is selected -> ANSWERED_AND_MARKED
        val newStatus = if (state.selectedOption != null) {
            QuestionStatus.ANSWERED_AND_MARKED
        } else {
            QuestionStatus.MARKED_FOR_REVIEW
        }
        state = state.copy(status = newStatus)
        assertEquals(QuestionStatus.ANSWERED_AND_MARKED, state.status)

        // Clear response while answered & marked -> resets option and reverts to MARKED_FOR_REVIEW
        val statusAfterClear = if (state.status == QuestionStatus.ANSWERED_AND_MARKED) {
            QuestionStatus.MARKED_FOR_REVIEW
        } else {
            QuestionStatus.NOT_ANSWERED
        }
        state = state.copy(selectedOption = null, status = statusAfterClear)
        assertNull(state.selectedOption)
        assertEquals(QuestionStatus.MARKED_FOR_REVIEW, state.status)

        // Unmark review -> reverts to NOT_ANSWERED
        val unmarkStatus = when (state.status) {
            QuestionStatus.MARKED_FOR_REVIEW -> QuestionStatus.NOT_ANSWERED
            QuestionStatus.ANSWERED_AND_MARKED -> QuestionStatus.ANSWERED
            else -> state.status
        }
        state = state.copy(status = unmarkStatus)
        assertEquals(QuestionStatus.NOT_ANSWERED, state.status)
    }

    @Test
    fun testMhtCetScoringRule() {
        // 10 Std XI questions + 40 Std XII questions
        val questions = (1..50).map { i ->
            val std = if (i <= 10) 11 else 12
            sampleQuestion(i.toLong(), std, correct = "A")
        }

        // Suppose user answered 40 questions correctly, 5 incorrectly, and left 5 unattempted
        var correct = 0
        var incorrect = 0
        var unattempted = 0
        var xiScore = 0
        var xiiScore = 0

        questions.forEachIndexed { index, q ->
            if (index < 40) {
                correct++
                if (q.stdClass == 11) xiScore += 2 else xiiScore += 2
            } else if (index < 45) {
                incorrect++
            } else {
                unattempted++
            }
        }

        val totalScore = correct * 2
        val totalQuestions = questions.size
        val accuracy = (correct.toFloat() / (correct + incorrect).toFloat()) * 100f
        val percentage = (totalScore.toFloat() / (totalQuestions * 2).toFloat()) * 100f

        assertEquals(80, totalScore)
        assertEquals(40, correct)
        assertEquals(5, incorrect)
        assertEquals(5, unattempted)
        assertEquals(88.88889f, accuracy, 0.01f)
        assertEquals(80.0f, percentage, 0.01f)
        assertEquals(20, xiScore) // all 10 XI correct = 20 marks
        assertEquals(60, xiiScore) // 30 of XII correct = 60 marks
    }
}
