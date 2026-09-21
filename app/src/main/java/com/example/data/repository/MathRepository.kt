package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DataStoreManager
import com.example.data.local.UserPreferences
import com.example.data.local.entity.*
import com.example.data.model.SyllabusConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MathRepository(
    private val database: AppDatabase,
    private val dataStoreManager: DataStoreManager,
    private val firestoreSyncRepository: FirestoreSyncRepository? = null
) {
    private val questionDao = database.questionDao()
    private val chapterDao = database.chapterDao()
    private val mistakeDao = database.mistakeDao()
    private val formulaDao = database.formulaDao()
    private val achievementDao = database.achievementDao()
    private val mockTestDao = database.mockTestDao()
    private val dailyChallengeDao = database.dailyChallengeDao()
    private val userProfileDao = database.userProfileDao()
    private val studentDao = database.studentDao()
    private val userLoginHistoryDao = database.userLoginHistoryDao()

    val userPreferencesFlow: Flow<UserPreferences> = dataStoreManager.userPreferencesFlow
    val latestUserProfile: Flow<UserProfileEntity?> = userProfileDao.getLatestUserProfile()
    val allUserProfiles: Flow<List<UserProfileEntity>> = userProfileDao.getAllUserProfiles()
    val allStudents: Flow<List<StudentEntity>> = studentDao.getAllStudents()
    val allLoginHistory: Flow<List<UserLoginHistoryEntity>> = userLoginHistoryDao.getAllLoginHistory()
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

        // 5. Cloud Firestore background sync
        triggerCloudSync()
    }

    fun triggerCloudSync() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prefs = dataStoreManager.userPreferencesFlow.firstOrNull() ?: return@launch
                firestoreSyncRepository?.syncProgressToCloud(prefs)
            } catch (_: Exception) {}
        }
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
        triggerCloudSync()
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
        triggerCloudSync()
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
        triggerCloudSync()
    }

    suspend fun updateHearts(hearts: Int) {
        dataStoreManager.setHearts(hearts)
    }

    suspend fun setActiveChapter(chapter: String) {
        dataStoreManager.setActiveChapter(chapter)
    }

    suspend fun hasUserProfile(uid: String): Boolean {
        return userProfileDao.findByUid(uid) != null
    }

    suspend fun initializeNewUserProgress() {
        dataStoreManager.resetUserProgress()
        chapterDao.resetAllChapterProgress()
        achievementDao.resetAllAchievements()
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

    // Admin Content Management Methods
    suspend fun addQuestion(question: QuestionEntity): Long {
        return questionDao.insertQuestion(question)
    }

    suspend fun deleteQuestion(id: Long) {
        questionDao.deleteQuestionById(id)
    }

    fun getRecentQuestions(limit: Int = 50): Flow<List<QuestionEntity>> {
        return questionDao.getRecentQuestions(limit)
    }

    suspend fun addFormula(formula: FormulaEntity): Long {
        return formulaDao.insertFormula(formula)
    }

    suspend fun deleteFormula(id: Long) {
        formulaDao.deleteFormulaById(id)
    }

    fun getRecentFormulas(limit: Int = 50): Flow<List<FormulaEntity>> {
        return formulaDao.getRecentFormulas(limit)
    }

    suspend fun getQuestionCount(): Int {
        return questionDao.getQuestionCount()
    }

    suspend fun getFormulaCount(): Int {
        return formulaDao.getFormulaCount()
    }

    // Admin Student Records & Management Methods
    suspend fun getStudentById(id: String): StudentEntity? {
        return studentDao.getStudentById(id)
    }

    suspend fun insertStudent(student: StudentEntity) {
        studentDao.insertStudent(student)
    }

    suspend fun updateStudent(student: StudentEntity) {
        studentDao.updateStudent(student)
    }

    suspend fun deleteStudent(id: String) {
        studentDao.deleteStudent(id)
    }

    suspend fun seedInitialStudentsIfEmpty() {
        val count = studentDao.getStudentCount()
        if (count > 0) return

        val sampleCohort = listOf(
            StudentEntity(
                studentId = "std_001",
                name = "Aarav Deshmukh",
                email = "aarav.deshmukh@gmail.com",
                standard = 12,
                targetExam = "MHT-CET 2026",
                enrollmentDate = System.currentTimeMillis() - 45L * 86400000L,
                lastActiveTimestamp = System.currentTimeMillis() - 15 * 60000L,
                xp = 4250,
                levelNumber = 6,
                levelTitle = "Differential Dynamo",
                streakDays = 12,
                totalSolved = 180,
                totalCorrect = 162,
                accuracy = 90.0f,
                studyTimeMinutes = 480,
                mockTestsTaken = 5,
                mockTestBestScore = 94,
                mockTestAvgScore = 88,
                chaptersMastered = 8,
                totalMistakes = 18,
                weakTopics = "Definite Integrals (Substitution)",
                strongTopics = "Vectors, 3D Geometry, Matrices, Logic",
                notes = "Consistent top performer. Strong speed in Paper 1 syllabus."
            ),
            StudentEntity(
                studentId = "std_002",
                name = "Sanika Kulkarni",
                email = "sanika.kulkarni@gmail.com",
                standard = 12,
                targetExam = "MHT-CET 2026",
                enrollmentDate = System.currentTimeMillis() - 30L * 86400000L,
                lastActiveTimestamp = System.currentTimeMillis() - 2 * 3600000L,
                xp = 3450,
                levelNumber = 5,
                levelTitle = "Vector Vanguard",
                streakDays = 8,
                totalSolved = 135,
                totalCorrect = 118,
                accuracy = 87.4f,
                studyTimeMinutes = 360,
                mockTestsTaken = 4,
                mockTestBestScore = 88,
                mockTestAvgScore = 82,
                chaptersMastered = 6,
                totalMistakes = 17,
                weakTopics = "Probability Distribution",
                strongTopics = "Trigonometric Functions, Pair of Straight Lines",
                notes = "Excellent formula retention. Focus on time pressure."
            ),
            StudentEntity(
                studentId = "std_003",
                name = "Rohan Patil",
                email = "rohan.patil@outlook.com",
                standard = 11,
                targetExam = "MHT-CET 2027",
                enrollmentDate = System.currentTimeMillis() - 20L * 86400000L,
                lastActiveTimestamp = System.currentTimeMillis() - 5 * 3600000L,
                xp = 2100,
                levelNumber = 4,
                levelTitle = "Concept Knight",
                streakDays = 5,
                totalSolved = 90,
                totalCorrect = 70,
                accuracy = 77.8f,
                studyTimeMinutes = 240,
                mockTestsTaken = 2,
                mockTestBestScore = 76,
                mockTestAvgScore = 72,
                chaptersMastered = 4,
                totalMistakes = 20,
                weakTopics = "Conics (Parabola focal properties)",
                strongTopics = "Straight Line, Trigonometry II",
                notes = "Good foundation in Class 11. Encouraged to take more mocks."
            ),
            StudentEntity(
                studentId = "std_004",
                name = "Ananya Joshi",
                email = "ananya.joshi@gmail.com",
                standard = 12,
                targetExam = "MHT-CET 2026",
                enrollmentDate = System.currentTimeMillis() - 60L * 86400000L,
                lastActiveTimestamp = System.currentTimeMillis() - 40 * 60000L,
                xp = 5120,
                levelNumber = 7,
                levelTitle = "Centum Scholar",
                streakDays = 21,
                totalSolved = 240,
                totalCorrect = 228,
                accuracy = 95.0f,
                studyTimeMinutes = 620,
                mockTestsTaken = 7,
                mockTestBestScore = 98,
                mockTestAvgScore = 93,
                chaptersMastered = 12,
                totalMistakes = 12,
                weakTopics = "Plane & Sphere intersection",
                strongTopics = "Differentiation, Applications of Derivatives, Matrices",
                notes = "Potential 99.9+ percentile candidate. Very fast calculations."
            ),
            StudentEntity(
                studentId = "std_005",
                name = "Aditya Shinde",
                email = "aditya.shinde@yahoo.com",
                standard = 11,
                targetExam = "MHT-CET 2027",
                enrollmentDate = System.currentTimeMillis() - 15L * 86400000L,
                lastActiveTimestamp = System.currentTimeMillis() - 24 * 3600000L,
                xp = 1580,
                levelNumber = 3,
                levelTitle = "Formula Apprentice",
                streakDays = 2,
                totalSolved = 65,
                totalCorrect = 47,
                accuracy = 72.3f,
                studyTimeMinutes = 160,
                mockTestsTaken = 1,
                mockTestBestScore = 68,
                mockTestAvgScore = 68,
                chaptersMastered = 2,
                totalMistakes = 18,
                weakTopics = "Limits & Continuity",
                strongTopics = "Sets & Relations, Functions",
                notes = "Needs extra practice on L'Hopital's rule and standard expansions."
            ),
            StudentEntity(
                studentId = "std_006",
                name = "Tanvi More",
                email = "tanvi.more@gmail.com",
                standard = 12,
                targetExam = "MHT-CET 2026",
                enrollmentDate = System.currentTimeMillis() - 25L * 86400000L,
                lastActiveTimestamp = System.currentTimeMillis() - 3 * 3600000L,
                xp = 2850,
                levelNumber = 4,
                levelTitle = "Concept Knight",
                streakDays = 6,
                totalSolved = 110,
                totalCorrect = 92,
                accuracy = 83.6f,
                studyTimeMinutes = 290,
                mockTestsTaken = 3,
                mockTestBestScore = 84,
                mockTestAvgScore = 79,
                chaptersMastered = 5,
                totalMistakes = 18,
                weakTopics = "Differential Equations (Linear Form)",
                strongTopics = "Linear Programming, Binomial Distribution",
                notes = "Steady progress. Recommended to revise integrating factor."
            )
        )
        studentDao.insertStudents(sampleCohort)
    }

    suspend fun syncCurrentStudentProgress(prefs: UserPreferences) {
        val uid = prefs.userId.ifBlank { "current_active_student" }
        val name = prefs.studentName.ifBlank { "Current Learner" }
        val email = prefs.userEmail.ifBlank { "student.active@cetquest.edu" }
        val totalSolved = prefs.totalSolved
        val totalCorrect = prefs.totalCorrect
        val accuracy = if (totalSolved > 0) (totalCorrect.toFloat() / totalSolved.toFloat()) * 100f else 0f

        val existing = studentDao.getStudentById(uid)
        val student = StudentEntity(
            studentId = uid,
            name = name,
            email = email,
            standard = 12,
            targetExam = "MHT-CET ${prefs.targetYear.ifBlank { "2026" }}",
            enrollmentDate = existing?.enrollmentDate ?: System.currentTimeMillis(),
            lastActiveTimestamp = System.currentTimeMillis(),
            xp = prefs.totalXp,
            levelNumber = prefs.currentLevel,
            levelTitle = when (prefs.currentLevel) {
                1 -> "Freshman Aspirant"
                2 -> "Arithmetic Novice"
                3 -> "Formula Apprentice"
                4 -> "Concept Knight"
                5 -> "Vector Vanguard"
                6 -> "Differential Dynamo"
                7 -> "Centum Scholar"
                else -> "MHT-CET Master"
            },
            streakDays = prefs.streakDays,
            totalSolved = totalSolved,
            totalCorrect = totalCorrect,
            accuracy = accuracy,
            studyTimeMinutes = prefs.studyTimeMinutes,
            mockTestsTaken = existing?.mockTestsTaken ?: 1,
            mockTestBestScore = existing?.mockTestBestScore ?: 80,
            mockTestAvgScore = existing?.mockTestAvgScore ?: 75,
            chaptersMastered = existing?.chaptersMastered ?: 3,
            totalMistakes = existing?.totalMistakes ?: 5,
            weakTopics = existing?.weakTopics ?: "Calculus, 3D Geometry",
            strongTopics = existing?.strongTopics ?: "Trigonometry, Matrices",
            notes = existing?.notes ?: "Active registered student."
        )
        studentDao.insertStudent(student)
    }

    suspend fun recordLogin(
        uid: String,
        email: String,
        displayName: String,
        loginMethod: String = "Email & Password"
    ) {
        val cleanEmail = email.trim().ifBlank { "aspirant@cetquest.edu" }
        val cleanName = displayName.trim().ifBlank {
            cleanEmail.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
        val now = System.currentTimeMillis()

        // 1. Record entry in user_login_history
        userLoginHistoryDao.insertLogin(
            UserLoginHistoryEntity(
                uid = uid,
                email = cleanEmail,
                displayName = cleanName,
                loginTimestamp = now,
                loginMethod = loginMethod,
                deviceModel = "Android Mobile Device",
                status = "Authenticated"
            )
        )

        // 2. Persist in user_profiles
        userProfileDao.insertOrUpdateProfile(
            UserProfileEntity(
                uid = uid,
                email = cleanEmail,
                displayName = cleanName,
                lastLoginAt = now
            )
        )

        // 3. Upsert student entity so student cohort reflects this active user
        val existingStudent = studentDao.getStudentById(uid)
        val student = StudentEntity(
            studentId = uid,
            name = cleanName,
            email = cleanEmail,
            standard = existingStudent?.standard ?: 12,
            targetExam = existingStudent?.targetExam ?: "MHT-CET 2026",
            enrollmentDate = existingStudent?.enrollmentDate ?: now,
            lastActiveTimestamp = now,
            xp = existingStudent?.xp ?: 150,
            levelNumber = existingStudent?.levelNumber ?: 1,
            levelTitle = existingStudent?.levelTitle ?: "Freshman Aspirant",
            streakDays = existingStudent?.streakDays ?: 1,
            totalSolved = existingStudent?.totalSolved ?: 0,
            totalCorrect = existingStudent?.totalCorrect ?: 0,
            accuracy = existingStudent?.accuracy ?: 0f,
            studyTimeMinutes = existingStudent?.studyTimeMinutes ?: 15,
            mockTestsTaken = existingStudent?.mockTestsTaken ?: 0,
            mockTestBestScore = existingStudent?.mockTestBestScore ?: 0,
            mockTestAvgScore = existingStudent?.mockTestAvgScore ?: 0,
            chaptersMastered = existingStudent?.chaptersMastered ?: 0,
            totalMistakes = existingStudent?.totalMistakes ?: 0,
            weakTopics = existingStudent?.weakTopics ?: "Calculus, Conics",
            strongTopics = existingStudent?.strongTopics ?: "Matrices, Logic",
            notes = "Active logged-in student. Logged in via $loginMethod"
        )
        studentDao.insertStudent(student)

        // 4. Update dataStore session
        dataStoreManager.saveAuthSession(uid, cleanEmail, cleanName)
    }

    suspend fun clearLoginHistory() {
        userLoginHistoryDao.clearHistory()
    }

    suspend fun deleteLoginRecord(id: Long) {
        userLoginHistoryDao.deleteLogin(id)
    }

    suspend fun seedInitialLoginsIfEmpty() {
        if (userLoginHistoryDao.getLoginCount() > 0) return
        val now = System.currentTimeMillis()
        val sampleLogins = listOf(
            UserLoginHistoryEntity(
                uid = "usr_sushant_001",
                email = "sushantschool2007@gmail.com",
                displayName = "Sushant Shinde",
                loginTimestamp = now - (3 * 60 * 1000L), // 3 mins ago
                loginMethod = "Email & Password",
                deviceModel = "Pixel 8 Pro",
                status = "Active"
            ),
            UserLoginHistoryEntity(
                uid = "usr_aarav_002",
                email = "aarav.deshmukh@gmail.com",
                displayName = "Aarav Deshmukh",
                loginTimestamp = now - (22 * 60 * 1000L), // 22 mins ago
                loginMethod = "Email & Password",
                deviceModel = "Samsung Galaxy S23",
                status = "Active"
            ),
            UserLoginHistoryEntity(
                uid = "usr_rohan_003",
                email = "rohan.patil@gmail.com",
                displayName = "Rohan Patil",
                loginTimestamp = now - (55 * 60 * 1000L), // 55 mins ago
                loginMethod = "Google Sign-in",
                deviceModel = "OnePlus 11",
                status = "Active"
            ),
            UserLoginHistoryEntity(
                uid = "usr_sanika_004",
                email = "sanika.kulkarni@gmail.com",
                displayName = "Sanika Kulkarni",
                loginTimestamp = now - (2 * 3600 * 1000L), // 2 hours ago
                loginMethod = "Email & Password",
                deviceModel = "Xiaomi 13T",
                status = "Active"
            ),
            UserLoginHistoryEntity(
                uid = "usr_tanvi_005",
                email = "tanvi.shinde@gmail.com",
                displayName = "Tanvi Shinde",
                loginTimestamp = now - (5 * 3600 * 1000L), // 5 hours ago
                loginMethod = "New Account Registration",
                deviceModel = "Nothing Phone 2",
                status = "Active"
            ),
            UserLoginHistoryEntity(
                uid = "usr_guest_006",
                email = "guest_8921@cetquest.edu",
                displayName = "CET Aspirant (Guest)",
                loginTimestamp = now - (8 * 3600 * 1000L), // 8 hours ago
                loginMethod = "Guest Session",
                deviceModel = "Android Tablet",
                status = "Guest Session"
            ),
            UserLoginHistoryEntity(
                uid = "usr_omkar_007",
                email = "omkar.joshi@gmail.com",
                displayName = "Omkar Joshi",
                loginTimestamp = now - (24 * 3600 * 1000L), // Yesterday
                loginMethod = "Email & Password",
                deviceModel = "Motorola Edge 40",
                status = "Logged Out"
            )
        )
        userLoginHistoryDao.insertLogins(sampleLogins)
    }
}


