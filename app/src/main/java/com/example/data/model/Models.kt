package com.example.data.model

enum class QuestionDifficulty(val level: Int, val label: String, val baseXP: Int) {
    LEVEL_1(1, "LEVEL 1 — Basic", 10),
    LEVEL_2(2, "LEVEL 2 — Concept", 15),
    LEVEL_3(3, "LEVEL 3 — CET Easy", 20),
    LEVEL_4(4, "LEVEL 4 — CET Medium", 25),
    LEVEL_5(5, "LEVEL 5 — CET Hard", 35),
    LEVEL_6(6, "LEVEL 6 — Challenge", 50);

    companion object {
        fun fromLevel(level: Int): QuestionDifficulty {
            return entries.firstOrNull { it.level == level } ?: LEVEL_3
        }
    }
}

enum class MistakeCategory(val title: String, val advice: String) {
    CONCEPT("Concept mistake", "Review fundamental definitions and theorems before attempting."),
    FORMULA("Formula mistake", "Brush up formula cards and sign conventions in Formula Book."),
    CALCULATION("Calculation mistake", "Double check intermediate algebra and arithmetic."),
    SILLY("Silly mistake", "Watch for edge cases, negative signs, and question constraints."),
    TIME_PRESSURE("Time-pressure mistake", "Use CET shortcut elimination to save time without rushing.")
}

enum class StudentMathLevel(val title: String, val badge: String, val description: String) {
    BEGINNER("Beginner", "Seedling", "Starting foundational revision for MHT-CET."),
    DEVELOPING("Developing", "Apprentice", "Good basics, need CET-level speed & shortcuts."),
    INTERMEDIATE("Intermediate", "Solver", "Strong concept grasp, aiming for 80+ marks."),
    ADVANCED("Advanced", "CET Master", "High speed & accuracy, aiming for 95-100 percentile.")
}

data class LevelInfo(
    val levelNumber: Int,
    val title: String,
    val minXP: Int,
    val maxXP: Int,
    val rankBadge: String
)

object GamificationConfig {
    val LEVELS = listOf(
        LevelInfo(1, "Beginner", 0, 150, "🌱"),
        LevelInfo(2, "Learner", 150, 400, "📘"),
        LevelInfo(3, "Solver", 400, 800, "⚡"),
        LevelInfo(4, "Fast Solver", 800, 1400, "🎯"),
        LevelInfo(5, "CET Fighter", 1400, 2200, "⚔️"),
        LevelInfo(6, "CET Expert", 2200, 3200, "🛡️"),
        LevelInfo(7, "Maths Master", 3200, 4500, "👑"),
        LevelInfo(8, "CET Champion", 4500, 100000, "🏆")
    )

    fun getLevelForXp(xp: Int): LevelInfo {
        for (i in LEVELS.indices.reversed()) {
            if (xp >= LEVELS[i].minXP) return LEVELS[i]
        }
        return LEVELS.first()
    }
}

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val percentile: String,
    val level: Int,
    val xp: Int,
    val accuracy: Int,
    val isCurrentUser: Boolean = false
)
