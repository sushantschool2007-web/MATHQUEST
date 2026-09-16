package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE chapter = :chapter ORDER BY id ASC")
    fun getQuestionsByChapter(chapter: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE chapter = :chapter ORDER BY id ASC")
    suspend fun getQuestionsByChapterSync(chapter: String): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionById(id: Long): QuestionEntity?

    @Query("SELECT * FROM questions WHERE stdClass = 11 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getQuestionsForMockClassXi(limit: Int = 10): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE stdClass = 12 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getQuestionsForMockClassXii(limit: Int = 40): List<QuestionEntity>

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestions(limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE id IN (:ids)")
    suspend fun getQuestionsByIds(ids: List<Long>): List<QuestionEntity>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)
}

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapter_progress ORDER BY stdClass ASC, chapterName ASC")
    fun getAllChapterProgress(): Flow<List<ChapterProgressEntity>>

    @Query("SELECT * FROM chapter_progress WHERE chapterName = :chapterName LIMIT 1")
    fun getChapterProgress(chapterName: String): Flow<ChapterProgressEntity?>

    @Query("SELECT * FROM chapter_progress WHERE chapterName = :chapterName LIMIT 1")
    suspend fun getChapterProgressSync(chapterName: String): ChapterProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: ChapterProgressEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<ChapterProgressEntity>)

    @Query("UPDATE chapter_progress SET questionsSolved = questionsSolved + 1, questionsCorrect = questionsCorrect + :isCorrect, mastery = :mastery, accuracy = :accuracy, lastStudiedTimestamp = :timestamp WHERE chapterName = :chapterName")
    suspend fun recordAttempt(chapterName: String, isCorrect: Int, mastery: Int, accuracy: Float, timestamp: Long)
}

@Dao
interface MistakeDao {
    @Query("SELECT * FROM mistakes ORDER BY timestamp DESC")
    fun getAllMistakes(): Flow<List<MistakeEntity>>

    @Query("SELECT * FROM mistakes WHERE isResolved = 0 ORDER BY timestamp DESC")
    fun getUnresolvedMistakes(): Flow<List<MistakeEntity>>

    @Query("SELECT * FROM mistakes WHERE chapter = :chapter ORDER BY timestamp DESC")
    fun getMistakesByChapter(chapter: String): Flow<List<MistakeEntity>>

    @Query("SELECT COUNT(*) FROM mistakes WHERE isResolved = 0")
    fun getUnresolvedMistakeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeEntity): Long

    @Query("UPDATE mistakes SET isResolved = 1 WHERE id = :id")
    suspend fun resolveMistake(id: Long)

    @Query("DELETE FROM mistakes WHERE id = :id")
    suspend fun deleteMistake(id: Long)
}

@Dao
interface FormulaDao {
    @Query("SELECT * FROM formulas ORDER BY stdClass ASC, chapter ASC, id ASC")
    fun getAllFormulas(): Flow<List<FormulaEntity>>

    @Query("SELECT * FROM formulas WHERE chapter = :chapter ORDER BY id ASC")
    fun getFormulasByChapter(chapter: String): Flow<List<FormulaEntity>>

    @Query("SELECT * FROM formulas WHERE isBookmarked = 1 ORDER BY id ASC")
    fun getBookmarkedFormulas(): Flow<List<FormulaEntity>>

    @Query("SELECT * FROM formulas WHERE title LIKE '%' || :query || '%' OR formula LIKE '%' || :query || '%' OR chapter LIKE '%' || :query || '%'")
    fun searchFormulas(query: String): Flow<List<FormulaEntity>>

    @Query("UPDATE formulas SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun setBookmark(id: Long, isBookmarked: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulas(formulas: List<FormulaEntity>)

    @Query("SELECT COUNT(*) FROM formulas")
    suspend fun getFormulaCount(): Int
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, id ASC")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedTimestamp = :timestamp WHERE id = :id AND isUnlocked = 0")
    suspend fun unlockAchievement(id: String, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievements(list: List<AchievementEntity>)
}

@Dao
interface MockTestDao {
    @Query("SELECT * FROM mock_test_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<MockTestResultEntity>>

    @Query("SELECT * FROM mock_test_results ORDER BY timestamp DESC LIMIT 1")
    fun getLatestResult(): Flow<MockTestResultEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: MockTestResultEntity): Long

    @Query("SELECT MAX(score) FROM mock_test_results")
    fun getBestScore(): Flow<Int?>
}

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenges WHERE dateKey = :dateKey LIMIT 1")
    fun getChallenge(dateKey: String): Flow<DailyChallengeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(challenge: DailyChallengeEntity)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE uid = :uid LIMIT 1")
    fun getUserProfile(uid: String): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun findByEmail(email: String): UserProfileEntity?

    @Query("SELECT * FROM user_profiles ORDER BY lastLoginAt DESC LIMIT 1")
    fun getLatestUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profiles SET passwordHash = :newHash, salt = :newSalt WHERE LOWER(email) = LOWER(:email)")
    suspend fun updatePassword(email: String, newHash: String, newSalt: String)

    @Query("DELETE FROM user_profiles WHERE uid = :uid")
    suspend fun deleteProfile(uid: String)

    @Query("DELETE FROM user_profiles")
    suspend fun clearAllProfiles()
}

