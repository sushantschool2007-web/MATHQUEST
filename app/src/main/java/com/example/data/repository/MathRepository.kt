package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DataStoreManager
import com.example.data.local.UserPreferences
import com.example.data.local.entity.*
import com.example.data.model.SyllabusConstants
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class MathRepository(
    private val database: AppDatabase,
    private val dataStoreManager: DataStoreManager
) {
    private val questionDao = database.questionDao()
    private val chapterDao = database.chapterDao()
    private val mistakeDao = database.mistakeDao()
    private val formulaDao = database.formulaDao()
    private val achievementDao = database.achievementDao()
    private val mockTestDao = database.mockTestDao()
    private val dailyChallengeDao = database.dailyChallengeDao()
    private val userProfileDao = database.userProfileDao()

    val userPreferencesFlow: Flow<UserPreferences> = dataStoreManager.userPreferencesFlow
    val latestUserProfile: Flow<UserProfileEntity?> = userProfileDao.getLatestUserProfile()
    val allChapterProgress: Flow<List<ChapterProgressEntity>> = chapterDao.getAllChapterProgress()
    val allMistakes: Flow<List<MistakeEntity>> = mistakeDao.getAllMistakes()
    val unresolvedMistakes: Flow<List<MistakeEntity>> = mistakeDao.getUnresolvedMistakes()
    val unresolvedMistakeCount: Flow<Int> = mistakeDao.getUnresolvedMistakeCount()
    val allFormulas: Flow<List<FormulaEntity>> = formulaDao.getAllFormulas()
    val bookmarkedFormulas: Flow<List<FormulaEntity>> = formulaDao.getBookmarkedFormulas()
    val allAchievements: Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()
    val allMockTestResults: Flow<List<MockTestResultEntity>> = mockTestDao.getAllResults()
    val latestMockResult: Flow<MockTestResultEntity?> = mockTestDao.getLatestResult()

    fun getQuestionsByChapter(chapter: String): Flow<List<QuestionEntity>> {
        return questionDao.getQuestionsByChapter(chapter)
    }

    suspend fun getQuestionsByChapterSync(chapter: String): List<QuestionEntity> {
        return questionDao.getQuestionsByChapterSync(chapter)
    }

    suspend fun getQuestionById(id: Long): QuestionEntity? {
        return questionDao.getQuestionById(id)
    }

    suspend fun getQuestionsByIds(ids: List<Long>): List<QuestionEntity> {
        return questionDao.getQuestionsByIds(ids)
    }

    suspend fun getRandomQuestions(count: Int): List<QuestionEntity> {
        val all = questionDao.getRandomQuestions(count)
        if (all.size < count) {
            // Repeat or pad if count exceeds initial questions
            val padded = mutableListOf<QuestionEntity>()
            while (padded.size < count && all.isNotEmpty()) {
                padded.addAll(all)
            }
            return padded.take(count)
        }
        return all
    }

    /**
     * Official 2026 MHT-CET Pattern:
     * Exactly 50 MCQs: 10 from Std. XI portion, 40 from Std. XII portion.
     */
    suspend fun generateMockTestQuestions(): List<QuestionEntity> {
        val xiQuestions = questionDao.getQuestionsForMockClassXi(10)
        val xiiQuestions = questionDao.getQuestionsForMockClassXii(40)

        val result = mutableListOf<QuestionEntity>()

        // Pad if needed to reach exactly 10 from Class XI
        var index = 0
        while (result.size < 10 && xiQuestions.isNotEmpty()) {
            result.add(xiQuestions[index % xiQuestions.size])
            index++
        }

        // Pad if needed to reach exactly 40 from Class XII
        index = 0
        val xiiList = mutableListOf<QuestionEntity>()
        while (xiiList.size < 40 && xiiQuestions.isNotEmpty()) {
            xiiList.add(xiiQuestions[index % xiiQuestions.size])
            index++
        }

        result.addAll(xiiList)
        return result
    }

    suspend fun recordQuestionAttempt(
        question: QuestionEntity,
        selectedOption: String,
        isCorrect: Boolean,
        timeSeconds: Int,
        mistakeType: String = "Calculation mistake"
    ) {
        // 1. Update user preferences (XP, solved count)
        val xpToAward = if (isCorrect) question.xp else 0
        if (xpToAward > 0) {
            dataStoreManager.addXp(xpToAward)
        }
        dataStoreManager.recordQuestionSolved(isCorrect, timeSeconds)
        dataStoreManager.updateStreak()

        // 2. Update chapter progress
        val currentProgress = chapterDao.getChapterProgressSync(question.chapter)
        val totalSolved = (currentProgress?.questionsSolved ?: 0) + 1
        val totalCorrect = (currentProgress?.questionsCorrect ?: 0) + (if (isCorrect) 1 else 0)
        val accuracy = (totalCorrect.toFloat() / totalSolved.toFloat()) * 100f
        val mastery = minOf(100, (accuracy * 0.7f + minOf(totalSolved * 3f, 30f)).toInt())

        chapterDao.recordAttempt(
            chapterName = question.chapter,
            isCorrect = if (isCorrect) 1 else 0,
            mastery = mastery,
            accuracy = accuracy,
            timestamp = System.currentTimeMillis()
        )

        // 3. Record mistake if incorrect
        if (!isCorrect) {
            mistakeDao.insertMistake(
                MistakeEntity(
                    questionId = question.id,
                    chapter = question.chapter,
                    topic = question.topic,
                    selectedOption = selectedOption,
                    correctOption = question.correctAnswer,
                    mistakeType = mistakeType
                )
            )
        }

        // 4. Check for first blood / streak achievements
        achievementDao.unlockAchievement("FIRST_BLOOD", System.currentTimeMillis())
    }

    suspend fun resolveMistake(mistakeId: Long) {
        mistakeDao.resolveMistake(mistakeId)
    }

    fun searchFormulas(query: String): Flow<List<FormulaEntity>> {
        return if (query.isBlank()) formulaDao.getAllFormulas() else formulaDao.searchFormulas(query)
    }

    suspend fun toggleFormulaBookmark(formulaId: Long, currentBookmarked: Boolean) {
        formulaDao.setBookmark(formulaId, !currentBookmarked)
    }

    suspend fun saveMockTestResult(result: MockTestResultEntity): Long {
        val id = mockTestDao.insertResult(result)
        dataStoreManager.addXp(result.score * 2) // Bonus XP for mock test
        achievementDao.unlockAchievement("MOCK_WARRIOR", System.currentTimeMillis())
        if (result.score >= 80) {
            achievementDao.unlockAchievement("CHAPTER_MASTER", System.currentTimeMillis())
        }
        return id
    }

    fun getDailyChallenge(dateKey: String): Flow<DailyChallengeEntity?> {
        return dailyChallengeDao.getChallenge(dateKey)
    }

    suspend fun completeDailyChallenge(dateKey: String, score: Int, xpEarned: Int) {
        dailyChallengeDao.insertOrUpdate(
            DailyChallengeEntity(
                dateKey = dateKey,
                totalQuestions = 10,
                isCompleted = true,
                scoreAchieved = score,
                xpAwarded = xpEarned,
                completedTimestamp = System.currentTimeMillis()
            )
        )
        dataStoreManager.addXp(xpEarned)
    }

    suspend fun saveOnboarding(
        studentName: String,
        targetYear: String,
        prepLevel: String,
        dailyGoalMinutes: Int,
        targetScore: Int,
        recommendedChapter: String
    ) {
        dataStoreManager.saveOnboarding(
            studentName, targetYear, prepLevel, dailyGoalMinutes, targetScore, recommendedChapter
        )
    }

    suspend fun updateHearts(hearts: Int) {
        dataStoreManager.setHearts(hearts)
    }

    suspend fun setActiveChapter(chapter: String) {
        dataStoreManager.setActiveChapter(chapter)
    }

    suspend fun saveUserProfile(
        uid: String,
        email: String,
        displayName: String,
        passwordHash: String = "",
        salt: String = ""
    ) {
        userProfileDao.insertOrUpdateProfile(
            UserProfileEntity(
                uid = uid,
                email = email,
                displayName = displayName,
                passwordHash = passwordHash,
                salt = salt,
                lastLoginAt = System.currentTimeMillis()
            )
        )
        dataStoreManager.saveAuthSession(uid, email, displayName)
    }

    suspend fun findProfileByEmail(email: String): UserProfileEntity? {
        return userProfileDao.findByEmail(email)
    }

    suspend fun updateUserPassword(email: String, newHash: String, newSalt: String) {
        userProfileDao.updatePassword(email, newHash, newSalt)
    }

    suspend fun clearUserSession() {
        dataStoreManager.clearAuthSession()
    }

    fun getUserProfile(uid: String): Flow<UserProfileEntity?> {
        return userProfileDao.getUserProfile(uid)
    }
}

