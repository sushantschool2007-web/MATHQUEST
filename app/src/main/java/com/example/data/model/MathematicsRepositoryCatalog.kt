package com.example.data.model

data class ValidationReport(
    val totalChapters: Int,
    val std11Part1Count: Int,
    val std11Part2Count: Int,
    val std12Part1Count: Int,
    val std12Part2Count: Int,
    val totalSubtopics: Int,
    val totalFormulas: Int,
    val totalSolvedExamples: Int,
    val mhtCetChaptersCount: Int,
    val jeeMainChaptersCount: Int,
    val duplicateChapterIds: List<String>,
    val emptyFieldWarnings: List<String>,
    val is100PercentComplete: Boolean
)

object MathematicsRepositoryCatalog {

    val allChapters: List<ComprehensiveChapter> by lazy {
        Std11Part1Catalog.chapters +
        Std11Part2Catalog.chapters +
        Std12Part1Catalog.chapters +
        Std12Part2Catalog.chapters
    }

    fun getChapterById(id: String): ComprehensiveChapter? {
        return allChapters.firstOrNull { it.chapterId.equals(id, ignoreCase = true) }
    }

    fun getChapterByName(name: String): ComprehensiveChapter? {
        return allChapters.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }

    fun getChaptersByStandard(standard: Int): List<ComprehensiveChapter> {
        return allChapters.filter { it.standard == standard }
    }

    fun getChaptersByStandardAndPart(standard: Int, part: Int): List<ComprehensiveChapter> {
        return allChapters.filter { it.standard == standard && it.part == part }
    }

    fun getMhtCetChapters(): List<ComprehensiveChapter> {
        return allChapters.filter { ch ->
            ExamMetadataRegistry.MHT_CET_STD11_CHAPTER_NAMES.contains(ch.name) ||
            ExamMetadataRegistry.MHT_CET_STD12_CHAPTER_NAMES.contains(ch.name) ||
            ch.standard == 12 // All 12th board chapters are in CET
        }
    }

    fun getJeeMainChapters(): List<ComprehensiveChapter> {
        return allChapters.filter { ch ->
            ExamMetadataRegistry.JEE_MAIN_SPECIFIC_FOCUS.contains(ch.name) ||
            ch.jeeMainRelevance.contains("JEE", ignoreCase = true)
        }
    }

    fun getAllFormulas(): List<FormulaItem> {
        return allChapters.flatMap { it.importantFormulas }
    }

    fun validateSyllabusHierarchy(): ValidationReport {
        val std11p1 = Std11Part1Catalog.chapters
        val std11p2 = Std11Part2Catalog.chapters
        val std12p1 = Std12Part1Catalog.chapters
        val std12p2 = Std12Part2Catalog.chapters
        val combined = allChapters

        val ids = combined.map { it.chapterId }
        val duplicates = ids.groupBy { it }.filter { it.value.size > 1 }.keys.toList()

        val emptyWarnings = mutableListOf<String>()
        combined.forEach { ch ->
            if (ch.subtopics.isEmpty()) emptyWarnings.add("Chapter ${ch.name} has no subtopics")
            if (ch.importantFormulas.isEmpty()) emptyWarnings.add("Chapter ${ch.name} has no formulas")
            if (ch.importantConcepts.isEmpty()) emptyWarnings.add("Chapter ${ch.name} has no concepts")
            if (ch.solvedExamples.isEmpty()) emptyWarnings.add("Chapter ${ch.name} has no solved examples")
        }

        return ValidationReport(
            totalChapters = combined.size,
            std11Part1Count = std11p1.size,
            std11Part2Count = std11p2.size,
            std12Part1Count = std12p1.size,
            std12Part2Count = std12p2.size,
            totalSubtopics = combined.sumOf { it.subtopics.size },
            totalFormulas = combined.sumOf { it.importantFormulas.size },
            totalSolvedExamples = combined.sumOf { it.solvedExamples.size },
            mhtCetChaptersCount = getMhtCetChapters().size,
            jeeMainChaptersCount = getJeeMainChapters().size,
            duplicateChapterIds = duplicates,
            emptyFieldWarnings = emptyWarnings,
            is100PercentComplete = duplicates.isEmpty() && emptyWarnings.isEmpty() && combined.size >= 40
        )
    }
}
