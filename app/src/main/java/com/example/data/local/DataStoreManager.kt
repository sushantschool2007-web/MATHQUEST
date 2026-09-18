package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cet_math_quest_prefs")

data class UserPreferences(
    val isOnboarded: Boolean,
    val studentName: String,
    val targetYear: String,
    val prepLevel: String,
    val dailyGoalMinutes: Int,
    val targetScore: Int,
    val totalXp: Int,
    val currentLevel: Int,
    val streakDays: Int,
    val lastActiveDate: String,
    val hearts: Int,
    val totalSolved: Int,
    val totalCorrect: Int,
    val studyTimeMinutes: Int,
    val activeChapter: String,
    val avatarId: Int,
    val isLoggedIn: Boolean = false,
    val userEmail: String = "",
    val userId: String = ""
)

class DataStoreManager(private val context: Context) {

    private object PreferencesKeys {
        val IS_ONBOARDED = booleanPreferencesKey("is_onboarded")
        val STUDENT_NAME = stringPreferencesKey("student_name")
        val TARGET_YEAR = stringPreferencesKey("target_year")
        val PREP_LEVEL = stringPreferencesKey("prep_level")
        val DAILY_GOAL_MINUTES = intPreferencesKey("daily_goal_minutes")
        val TARGET_SCORE = intPreferencesKey("target_score")
        val TOTAL_XP = intPreferencesKey("total_xp")
        val CURRENT_LEVEL = intPreferencesKey("current_level")
        val STREAK_DAYS = intPreferencesKey("streak_days")
        val LAST_ACTIVE_DATE = stringPreferencesKey("last_active_date")
        val HEARTS = intPreferencesKey("hearts")
        val TOTAL_SOLVED = intPreferencesKey("total_solved")
        val TOTAL_CORRECT = intPreferencesKey("total_correct")
        val STUDY_TIME_MINUTES = intPreferencesKey("study_time_minutes")
        val ACTIVE_CHAPTER = stringPreferencesKey("active_chapter")
        val AVATAR_ID = intPreferencesKey("avatar_id")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_ID = stringPreferencesKey("user_id")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        UserPreferences(
            isOnboarded = preferences[PreferencesKeys.IS_ONBOARDED] ?: false,
            studentName = preferences[PreferencesKeys.STUDENT_NAME] ?: "Aspirant",
            targetYear = preferences[PreferencesKeys.TARGET_YEAR] ?: "2026",
            prepLevel = preferences[PreferencesKeys.PREP_LEVEL] ?: "Developing",
            dailyGoalMinutes = preferences[PreferencesKeys.DAILY_GOAL_MINUTES] ?: 30,
            targetScore = preferences[PreferencesKeys.TARGET_SCORE] ?: 90,
            totalXp = preferences[PreferencesKeys.TOTAL_XP] ?: 0,
            currentLevel = preferences[PreferencesKeys.CURRENT_LEVEL] ?: 1,
            streakDays = preferences[PreferencesKeys.STREAK_DAYS] ?: 0,
            lastActiveDate = preferences[PreferencesKeys.LAST_ACTIVE_DATE] ?: "",
            hearts = preferences[PreferencesKeys.HEARTS] ?: 5,
            totalSolved = preferences[PreferencesKeys.TOTAL_SOLVED] ?: 0,
            totalCorrect = preferences[PreferencesKeys.TOTAL_CORRECT] ?: 0,
            studyTimeMinutes = preferences[PreferencesKeys.STUDY_TIME_MINUTES] ?: 0,
            activeChapter = preferences[PreferencesKeys.ACTIVE_CHAPTER] ?: "Trigonometry II",
            avatarId = preferences[PreferencesKeys.AVATAR_ID] ?: 0,
            isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false,
            userEmail = preferences[PreferencesKeys.USER_EMAIL] ?: "",
            userId = preferences[PreferencesKeys.USER_ID] ?: ""
        )
    }

    suspend fun saveOnboarding(
        studentName: String,
        targetYear: String,
        prepLevel: String,
        dailyGoalMinutes: Int,
        targetScore: Int,
        recommendedChapter: String
    ) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDED] = true
            preferences[PreferencesKeys.STUDENT_NAME] = studentName
            preferences[PreferencesKeys.TARGET_YEAR] = targetYear
            preferences[PreferencesKeys.PREP_LEVEL] = prepLevel
            preferences[PreferencesKeys.DAILY_GOAL_MINUTES] = dailyGoalMinutes
            preferences[PreferencesKeys.TARGET_SCORE] = targetScore
            preferences[PreferencesKeys.ACTIVE_CHAPTER] = recommendedChapter
            preferences[PreferencesKeys.LAST_ACTIVE_DATE] = today
            preferences[PreferencesKeys.STREAK_DAYS] = 1
            preferences[PreferencesKeys.HEARTS] = 5
        }
    }

    suspend fun addXp(amount: Int) {
        context.dataStore.edit { preferences ->
            val currentXp = (preferences[PreferencesKeys.TOTAL_XP] ?: 0) + amount
            preferences[PreferencesKeys.TOTAL_XP] = currentXp

            // Calculate level
            val level = when {
                currentXp >= 4500 -> 8
                currentXp >= 3200 -> 7
                currentXp >= 2200 -> 6
                currentXp >= 1400 -> 5
                currentXp >= 800 -> 4
                currentXp >= 400 -> 3
                currentXp >= 150 -> 2
                else -> 1
            }
            preferences[PreferencesKeys.CURRENT_LEVEL] = level
        }
    }

    suspend fun recordQuestionSolved(isCorrect: Boolean, timeSeconds: Int) {
        context.dataStore.edit { preferences ->
            val solved = (preferences[PreferencesKeys.TOTAL_SOLVED] ?: 0) + 1
            val correct = (preferences[PreferencesKeys.TOTAL_CORRECT] ?: 0) + (if (isCorrect) 1 else 0)
            val studyMins = (preferences[PreferencesKeys.STUDY_TIME_MINUTES] ?: 0) + maxOf(1, timeSeconds / 60)

            preferences[PreferencesKeys.TOTAL_SOLVED] = solved
            preferences[PreferencesKeys.TOTAL_CORRECT] = correct
            preferences[PreferencesKeys.STUDY_TIME_MINUTES] = studyMins
        }
    }

    suspend fun updateStreak() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        context.dataStore.edit { preferences ->
            val lastDate = preferences[PreferencesKeys.LAST_ACTIVE_DATE] ?: ""
            if (lastDate != today) {
                val currentStreak = preferences[PreferencesKeys.STREAK_DAYS] ?: 1
                preferences[PreferencesKeys.STREAK_DAYS] = currentStreak + 1
                preferences[PreferencesKeys.LAST_ACTIVE_DATE] = today
            }
        }
    }

    suspend fun setActiveChapter(chapter: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_CHAPTER] = chapter
        }
    }

    suspend fun setHearts(hearts: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HEARTS] = hearts.coerceIn(0, 5)
        }
    }

    suspend fun setAvatar(avatarId: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AVATAR_ID] = avatarId
        }
    }

    suspend fun saveAuthSession(uid: String, email: String, name: String?) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
            preferences[PreferencesKeys.USER_ID] = uid
            preferences[PreferencesKeys.USER_EMAIL] = email
            if (!name.isNullOrBlank()) {
                preferences[PreferencesKeys.STUDENT_NAME] = name
            }
        }
    }

    suspend fun clearAuthSession() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = false
            preferences[PreferencesKeys.USER_ID] = ""
            preferences[PreferencesKeys.USER_EMAIL] = ""
        }
    }

    suspend fun updateUserProfile(name: String, email: String) {
        context.dataStore.edit { preferences ->
            if (name.isNotBlank()) {
                preferences[PreferencesKeys.STUDENT_NAME] = name
            }
            if (email.isNotBlank()) {
                preferences[PreferencesKeys.USER_EMAIL] = email
            }
        }
    }

    suspend fun mergeCloudProgress(
        cloudXp: Int,
        cloudLevel: Int,
        cloudSolved: Int,
        cloudCorrect: Int,
        cloudStreak: Int
    ) {
        context.dataStore.edit { preferences ->
            val currentXp = preferences[PreferencesKeys.TOTAL_XP] ?: 0
            if (cloudXp > currentXp) {
                preferences[PreferencesKeys.TOTAL_XP] = cloudXp
                preferences[PreferencesKeys.CURRENT_LEVEL] = maxOf(preferences[PreferencesKeys.CURRENT_LEVEL] ?: 1, cloudLevel)
                preferences[PreferencesKeys.TOTAL_SOLVED] = maxOf(preferences[PreferencesKeys.TOTAL_SOLVED] ?: 0, cloudSolved)
                preferences[PreferencesKeys.TOTAL_CORRECT] = maxOf(preferences[PreferencesKeys.TOTAL_CORRECT] ?: 0, cloudCorrect)
                preferences[PreferencesKeys.STREAK_DAYS] = maxOf(preferences[PreferencesKeys.STREAK_DAYS] ?: 0, cloudStreak)
            }
        }
    }

    suspend fun resetUserProgress() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOTAL_XP] = 0
            preferences[PreferencesKeys.CURRENT_LEVEL] = 1
            preferences[PreferencesKeys.TOTAL_SOLVED] = 0
            preferences[PreferencesKeys.TOTAL_CORRECT] = 0
            preferences[PreferencesKeys.STUDY_TIME_MINUTES] = 0
            preferences[PreferencesKeys.STREAK_DAYS] = 0
            preferences[PreferencesKeys.HEARTS] = 5
        }
    }
}

