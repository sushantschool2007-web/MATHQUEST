package com.example

import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.StudentEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminLogicTest {

    private fun sampleStudents(): List<StudentEntity> = listOf(
        StudentEntity(
            studentId = "std_1",
            name = "Aarav Deshmukh",
            email = "aarav@gmail.com",
            standard = 12,
            targetExam = "MHT-CET 2026",
            accuracy = 92.5f,
            totalSolved = 200,
            totalCorrect = 185,
            xp = 4500
        ),
        StudentEntity(
            studentId = "std_2",
            name = "Rohan Patil",
            email = "rohan@gmail.com",
            standard = 11,
            targetExam = "MHT-CET 2027",
            accuracy = 70.0f,
            totalSolved = 80,
            totalCorrect = 56,
            xp = 1800
        ),
        StudentEntity(
            studentId = "std_3",
            name = "Sanika Kulkarni",
            email = "sanika@gmail.com",
            standard = 12,
            targetExam = "MHT-CET 2026",
            accuracy = 86.0f,
            totalSolved = 150,
            totalCorrect = 129,
            xp = 3400
        )
    )

    @Test
    fun testStudentFilterByStandard() {
        val list = sampleStudents()
        val class12 = list.filter { it.standard == 12 }
        val class11 = list.filter { it.standard == 11 }

        assertEquals(2, class12.size)
        assertEquals(1, class11.size)
    }

    @Test
    fun testStudentFilterByPerformanceThreshold() {
        val list = sampleStudents()
        val topPerformers = list.filter { it.accuracy >= 85f }
        val needsSupport = list.filter { it.accuracy < 75f }

        assertEquals(2, topPerformers.size)
        assertEquals("Aarav Deshmukh", topPerformers.first().name)
        assertEquals(1, needsSupport.size)
        assertEquals("Rohan Patil", needsSupport.first().name)
    }

    @Test
    fun testQuestionContentValidation() {
        val newQuestion = QuestionEntity(
            stdClass = 12,
            chapter = "Matrices",
            topic = "Inverses",
            subtopic = "Adjoint Method",
            difficulty = "LEVEL 4 — CET Medium",
            question = "Find the inverse of [[1, 2], [3, 4]]",
            optionA = "[[-2, 1], [1.5, -0.5]]",
            optionB = "[[1, 0], [0, 1]]",
            optionC = "[[4, -2], [-3, 1]]",
            optionD = "Does not exist",
            correctAnswer = "A",
            explanation = "A^-1 = 1/|A| * adj(A)",
            formula = "A^{-1} = \\frac{1}{|A|} \\text{adj}(A)",
            shortcut = "Direct 2x2 inverse swap diagonal and negate off-diagonal",
            hint1 = "Find determinant first: 1*4 - 2*3 = -2",
            hint2 = "Compute adjoint matrix",
            hint3 = "Divide adj(A) by -2"
        )

        assertEquals("A", newQuestion.correctAnswer)
        assertEquals(12, newQuestion.stdClass)
        assertTrue(newQuestion.question.isNotBlank())
        assertTrue(newQuestion.optionA.isNotBlank())
    }

    @Test
    fun testFormulaVaultValidation() {
        val newFormula = FormulaEntity(
            stdClass = 12,
            chapter = "Vectors",
            topic = "Triple Product",
            title = "Scalar Triple Product Volume",
            formula = "V = |\\vec{a} \\cdot (\\vec{b} \\times \\vec{c})|",
            variablesMeaning = "a, b, c are coterminous edges of a parallelopiped",
            whenToUse = "Volume of parallelopiped or coplanar vectors check",
            example = "If STP = 0, vectors are coplanar",
            shortcut = "Compute determinant of component vectors"
        )

        assertEquals("Vectors", newFormula.chapter)
        assertEquals(12, newFormula.stdClass)
        assertTrue(newFormula.formula.contains("vec{a}"))
    }
}
