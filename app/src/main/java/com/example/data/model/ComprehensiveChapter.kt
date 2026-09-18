package com.example.data.model

data class SubtopicItem(
    val id: String,
    val name: String,
    val concepts: List<String> = emptyList()
)

data class FormulaItem(
    val formulaId: String,
    val chapterId: String,
    val topicId: String,
    val formulaName: String,
    val formula: String,
    val variables: String = "",
    val description: String,
    val example: String = "",
    val difficulty: String = "Medium", // Easy, Medium, Hard, JEE Advanced
    val examTags: List<String> = listOf("MHT-CET", "JEE-MAIN")
)

data class SolvedExampleItem(
    val id: String,
    val title: String,
    val problem: String,
    val solution: String,
    val keyConcept: String = ""
)

data class ComprehensiveChapter(
    val chapterId: String,
    val name: String,
    val standard: Int, // 11 or 12
    val part: Int, // 1 or 2
    val chapterNumber: Int,
    val description: String,
    val subtopics: List<SubtopicItem>,
    val importantFormulas: List<FormulaItem>,
    val importantConcepts: List<String>,
    val solvedExamples: List<SolvedExampleItem>,
    val difficultyLevel: String, // Easy, Medium, Hard
    val mhtCetRelevance: String,
    val jeeMainRelevance: String,
    val cetWeightageMarks: Int = 4
)
