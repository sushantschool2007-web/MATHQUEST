package com.example.data.local

import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.ChapterProgressEntity
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.model.MathematicsRepositoryCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseInitializer {

    suspend fun populateIfEmpty(database: AppDatabase) = withContext(Dispatchers.IO) {
        val questionDao = database.questionDao()
        val chapterDao = database.chapterDao()
        val formulaDao = database.formulaDao()
        val achievementDao = database.achievementDao()

        val count = questionDao.getQuestionCount()
        if (count > 0) return@withContext

        // 1. Prepopulate All 42 Chapters from MathematicsRepositoryCatalog with clean 0 progress
        val allComprehensiveChapters = MathematicsRepositoryCatalog.allChapters
        val chapterEntities = allComprehensiveChapters.mapIndexed { index, comp ->
            ChapterProgressEntity(
                chapterName = comp.name,
                stdClass = comp.standard,
                totalQuestions = 15,
                questionsSolved = 0,
                questionsCorrect = 0,
                accuracy = 0f,
                mastery = 0,
                isUnlocked = index < 5 || comp.standard == 11, // Unlock initial chapters
                unlockedDifficulty = 1,
                bestScore = 0,
                lastStudiedTimestamp = 0L
            )
        }
        chapterDao.insertAll(chapterEntities)

        // 2. Prepopulate Achievements
        val initialAchievements = listOf(
            AchievementEntity(
                id = "FIRST_BLOOD",
                title = "First Step",
                description = "Solve your very first MHT-CET mathematics question.",
                iconEmoji = "🎯",
                xpReward = 50,
                isUnlocked = false
            ),
            AchievementEntity(
                id = "STREAK_3",
                title = "Consistent Aspirant",
                description = "Maintain a 3-day continuous study streak.",
                iconEmoji = "🔥",
                xpReward = 100,
                isUnlocked = false
            ),
            AchievementEntity(
                id = "COMBO_X5",
                title = "Combo Striker",
                description = "Answer 5 consecutive questions correctly.",
                iconEmoji = "⚡",
                xpReward = 150,
                isUnlocked = false
            ),
            AchievementEntity(
                id = "CHAPTER_MASTER",
                title = "Chapter Master",
                description = "Reach 80%+ mastery in any MHT-CET chapter.",
                iconEmoji = "🏆",
                xpReward = 200,
                isUnlocked = false
            ),
            AchievementEntity(
                id = "FORMULA_SAVVY",
                title = "Formula Collector",
                description = "Bookmark 5 or more formulas in your Formula Book.",
                iconEmoji = "📖",
                xpReward = 75,
                isUnlocked = false
            ),
            AchievementEntity(
                id = "BOSS_SLAYER",
                title = "Boss Slayer",
                description = "Defeat the Chapter Boss in Boss Battle.",
                iconEmoji = "⚔️",
                xpReward = 250,
                isUnlocked = false
            ),
            AchievementEntity(
                id = "SPEED_DEMON",
                title = "Speed Demon",
                description = "Complete a Speed Run with 80%+ accuracy.",
                iconEmoji = "⏱️",
                xpReward = 150,
                isUnlocked = false
            ),
            AchievementEntity(
                id = "MOCK_WARRIOR",
                title = "Mock Veteran",
                description = "Complete a full 50-question MHT-CET mock test.",
                iconEmoji = "🛡️",
                xpReward = 300,
                isUnlocked = false
            )
        )
        achievementDao.insertAchievements(initialAchievements)

        // 3. Prepopulate Formulas for ALL Chapters across Std 11 & Std 12
        val allFormulas = allComprehensiveChapters.flatMap { ch ->
            ch.importantFormulas.mapIndexed { idx, item ->
                FormulaEntity(
                    stdClass = ch.standard,
                    chapter = ch.name,
                    topic = item.topicId,
                    title = item.formulaName,
                    formula = item.formula,
                    variablesMeaning = item.variables.ifBlank { "Standard mathematical symbols." },
                    whenToUse = item.description,
                    example = item.example.ifBlank { "Direct substitution into standard format." },
                    shortcut = if (item.difficulty == "Hard" || item.difficulty == "JEE Advanced") "High-yield exam shortcut" else "",
                    isBookmarked = (idx == 0 && (ch.chapterNumber == 1 || ch.chapterNumber == 2))
                )
            }
        }
        formulaDao.insertFormulas(allFormulas)

        // 4. Prepopulate authentic MHT-CET & JEE questions
        val questions = SeedQuestions.getQuestions()
        questionDao.insertQuestions(questions)
    }
}
