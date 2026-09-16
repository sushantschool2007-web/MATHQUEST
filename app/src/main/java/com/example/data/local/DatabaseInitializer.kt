package com.example.data.local

import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.ChapterProgressEntity
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.model.SyllabusConstants
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

        // 1. Prepopulate Chapters with clean 0 progress for new users
        val chapterEntities = SyllabusConstants.ALL_CHAPTERS.mapIndexed { index, meta ->
            ChapterProgressEntity(
                chapterName = meta.name,
                stdClass = meta.stdClass,
                totalQuestions = 15,
                questionsSolved = 0,
                questionsCorrect = 0,
                accuracy = 0f,
                mastery = 0,
                isUnlocked = index < 3 || meta.stdClass == 11, // Unlock initial chapters
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
                description = "Defeat the Trigonometry Titan in Boss Battle.",
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

        // 3. Prepopulate Formulas
        val initialFormulas = listOf(
            FormulaEntity(
                stdClass = 11,
                chapter = "Trigonometry II",
                topic = "Multiple Angles",
                title = "sin 2θ and cos 2θ Identities",
                formula = "sin 2θ = 2 sin θ cos θ = (2 tan θ) / (1 + tan² θ)\ncos 2θ = cos² θ - sin² θ = 2 cos² θ - 1 = 1 - 2 sin² θ = (1 - tan² θ) / (1 + tan² θ)",
                variablesMeaning = "θ is any real angle where tan θ is defined.",
                whenToUse = "When converting double angle to single angle, or expressing sin/cos in terms of tan for algebraic simplification.",
                example = "If tan θ = 1/2, then sin 2θ = 2(1/2)/(1 + 1/4) = 1/(5/4) = 4/5.",
                shortcut = "Use 1 - cos 2θ = 2 sin² θ and 1 + cos 2θ = 2 cos² θ directly for square root limits.",
                isBookmarked = true
            ),
            FormulaEntity(
                stdClass = 11,
                chapter = "Trigonometry II",
                topic = "Factorization",
                title = "Transformation of Sum/Diff to Product",
                formula = "sin C + sin D = 2 sin((C+D)/2) cos((C-D)/2)\nsin C - sin D = 2 cos((C+D)/2) sin((C-D)/2)\ncos C + cos D = 2 cos((C+D)/2) cos((C-D)/2)\ncos C - cos D = -2 sin((C+D)/2) sin((C-D)/2)",
                variablesMeaning = "C and D are distinct angles.",
                whenToUse = "When simplifying trigonometric sums in fractions, triangles, or limits.",
                example = "sin 75° - sin 15° = 2 cos 45° sin 30° = 2(1/√2)(1/2) = 1/√2.",
                shortcut = "Remember cos C - cos D has a negative sign: 2 sin((C+D)/2) sin((D-C)/2).",
                isBookmarked = false
            ),
            FormulaEntity(
                stdClass = 11,
                chapter = "Straight Line",
                topic = "Distance Formula",
                title = "Perpendicular Distance of Point from Line",
                formula = "d = |a x₁ + b y₁ + c| / √(a² + b²)",
                variablesMeaning = "(x₁, y₁) is the point coordinates; ax + by + c = 0 is the line equation.",
                whenToUse = "Finding distance from point to line or distance between parallel lines.",
                example = "Distance of (2, -1) from 3x - 4y + 5 = 0 is |3(2) - 4(-1) + 5| / √(3² + (-4)²) = |6 + 4 + 5|/5 = 15/5 = 3.",
                shortcut = "Distance between parallel lines ax + by + c₁ = 0 and ax + by + c₂ = 0 is |c₁ - c₂| / √(a² + b²).",
                isBookmarked = true
            ),
            FormulaEntity(
                stdClass = 11,
                chapter = "Circle",
                topic = "General Equation",
                title = "Center and Radius of General Circle",
                formula = "x² + y² + 2gx + 2fy + c = 0\nCenter = (-g, -f), Radius r = √(g² + f² - c)",
                variablesMeaning = "g, f, c are real constants with g² + f² - c ≥ 0.",
                whenToUse = "Extracting geometric properties from algebraic circle equation.",
                example = "For x² + y² - 4x + 6y - 12 = 0: 2g = -4 ⇒ g = -2, 2f = 6 ⇒ f = 3. Center = (2, -3), r = √(4 + 9 - (-12)) = √25 = 5.",
                shortcut = "Center is simply (-half coeff of x, -half coeff of y).",
                isBookmarked = false
            ),
            FormulaEntity(
                stdClass = 11,
                chapter = "Limits",
                topic = "Standard Limits",
                title = "Trigonometric and Exponential Standard Limits",
                formula = "lim(x→0) (sin x)/x = 1 (x in radians)\nlim(x→0) (tan x)/x = 1\nlim(x→0) (eˣ - 1)/x = 1\nlim(x→0) (aˣ - 1)/x = ln a",
                variablesMeaning = "x approaches 0.",
                whenToUse = "Evaluating 0/0 indeterminate forms involving trig or exponential functions.",
                example = "lim(x→0) (sin 5x)/(3x) = (5/3) lim(x→0) (sin 5x)/(5x) = 5/3.",
                shortcut = "Use 1 - cos(kx) ≈ (k²x²)/2 as x → 0 to eliminate trig terms instantly.",
                isBookmarked = true
            ),
            FormulaEntity(
                stdClass = 12,
                chapter = "Matrices",
                topic = "Inverse Matrix",
                title = "Inverse of 2x2 Matrix Shortcut",
                formula = "A = [[a, b], [c, d]]\nA⁻¹ = (1 / det(A)) * [[d, -b], [-c, a]], where det(A) = ad - bc ≠ 0",
                variablesMeaning = "a, b, c, d are elements of matrix A.",
                whenToUse = "Instantly computing inverse of 2x2 matrix without cofactor matrix.",
                example = "If A = [[2, 3], [1, 4]], det(A) = 8 - 3 = 5. A⁻¹ = (1/5) * [[4, -3], [-1, 2]].",
                shortcut = "Swap diagonal elements (a and d), negate off-diagonal elements (b and c), divide by determinant.",
                isBookmarked = true
            ),
            FormulaEntity(
                stdClass = 12,
                chapter = "Differentiation",
                topic = "Standard Rules",
                title = "Derivative of Inverse Trigonometric Functions",
                formula = "d/dx(sin⁻¹ x) = 1 / √(1 - x²)\nd/dx(cos⁻¹ x) = -1 / √(1 - x²)\nd/dx(tan⁻¹ x) = 1 / (1 + x²)\nd/dx(cot⁻¹ x) = -1 / (1 + x²)",
                variablesMeaning = "|x| < 1 for sin⁻¹ and cos⁻¹.",
                whenToUse = "Differentiating inverse trig functions and solving differential equations.",
                example = "d/dx(tan⁻¹(2x)) = [1 / (1 + (2x)²)] * 2 = 2 / (1 + 4x²).",
                shortcut = "If y = tan⁻¹((3x - x³)/(1 - 3x²)), put x = tan θ to get y = 3θ = 3 tan⁻¹ x, so dy/dx = 3/(1+x²).",
                isBookmarked = false
            ),
            FormulaEntity(
                stdClass = 12,
                chapter = "Vectors",
                topic = "Scalar Triple Product",
                title = "Box Product and Coplanarity",
                formula = "[a b c] = a · (b × c) = det([[a₁, a₂, a₃], [b₁, b₂, b₃], [c₁, c₂, c₃]])\nVolume of parallelepiped = |[a b c]|",
                variablesMeaning = "a, b, c are three non-zero 3D vectors.",
                whenToUse = "Testing coplanarity of 3 vectors or finding volume of parallelepiped/tetrahedron.",
                example = "If [a b c] = 0, vectors a, b, c are coplanar.",
                shortcut = "Volume of tetrahedron = (1/6) |[a b c]|.",
                isBookmarked = true
            ),
            FormulaEntity(
                stdClass = 12,
                chapter = "Definite Integration",
                topic = "Properties",
                title = "King's Rule (Integral Property)",
                formula = "∫[a to b] f(x) dx = ∫[a to b] f(a + b - x) dx\nSpecial case: ∫[0 to a] f(x) dx = ∫[0 to a] f(a - x) dx",
                variablesMeaning = "f(x) is continuous on [a, b].",
                whenToUse = "Symmetric definite integrals with sin/cos or difficult denominators.",
                example = "I = ∫[0 to π/2] (sin x)/(sin x + cos x) dx. Using property: I = ∫[0 to π/2] (cos x)/(cos x + sin x) dx. Adding: 2I = ∫[0 to π/2] 1 dx = π/2 ⇒ I = π/4.",
                shortcut = "Whenever numerator + modified numerator = denominator, the integral is simply (b - a)/2.",
                isBookmarked = true
            )
        )
        formulaDao.insertFormulas(initialFormulas)

        // 4. Prepopulate authentic MHT-CET Questions
        val questions = SeedQuestions.getQuestions()
        questionDao.insertQuestions(questions)
    }
}
