package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stdClass: Int, // 11 or 12
    val chapter: String,
    val topic: String,
    val subtopic: String,
    val difficulty: String, // "LEVEL 1 — Basic", "LEVEL 2 — Concept", "LEVEL 3 — CET Easy", "LEVEL 4 — CET Medium", "LEVEL 5 — CET Hard", "LEVEL 6 — Challenge"
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String, // "A", "B", "C", or "D"
    val explanation: String,
    val formula: String,
    val shortcut: String,
    val hint1: String,
    val hint2: String,
    val hint3: String,
    val estimatedTime: Int = 45, // seconds
    val xp: Int = 20,
    val tags: String = "",
    val isPyq: Boolean = false,
    val pyqYear: String = "",
    // Teach Me specific step by step breakdown
    val teachMeGiven: String = "",
    val teachMeWeNeed: String = "",
    val teachMeFormula: String = "",
    val teachMeSubstitution: String = "",
    val teachMeCalculation: String = "",
    val teachMeFinalAnswer: String = "",
    val teachMeShortcut: String = ""
)

@Entity(tableName = "chapter_progress")
data class ChapterProgressEntity(
    @PrimaryKey
    val chapterName: String,
    val stdClass: Int,
    val totalQuestions: Int = 0,
    val questionsSolved: Int = 0,
    val questionsCorrect: Int = 0,
    val accuracy: Float = 0f,
    val mastery: Int = 0, // 0 - 100%
    val isUnlocked: Boolean = true,
    val unlockedDifficulty: Int = 1, // Level 1 to 6
    val bestScore: Int = 0,
    val lastStudiedTimestamp: Long = 0L
)

@Entity(tableName = "mistakes")
data class MistakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionId: Long,
    val chapter: String,
    val topic: String,
    val selectedOption: String,
    val correctOption: String,
    val mistakeType: String, // "Concept mistake", "Formula mistake", "Calculation mistake", "Silly mistake", "Time-pressure mistake"
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)

@Entity(tableName = "formulas")
data class FormulaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stdClass: Int,
    val chapter: String,
    val topic: String,
    val title: String,
    val formula: String,
    val variablesMeaning: String,
    val whenToUse: String,
    val example: String,
    val shortcut: String,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long = 0L
)

@Entity(tableName = "mock_test_results")
data class MockTestResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val score: Int, // Out of 100
    val percentage: Float,
    val accuracy: Float,
    val correctCount: Int,
    val incorrectCount: Int,
    val unattemptedCount: Int,
    val totalQuestions: Int = 50,
    val classXiScore: Int,
    val classXiiScore: Int,
    val timeTakenSeconds: Int,
    val fastestQuestionTimeSeconds: Int,
    val slowestQuestionTimeSeconds: Int,
    val strongTopics: String,
    val weakTopics: String,
    val recommendedRevision: String,
    val prepLevelEstimate: String
)

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey
    val dateKey: String, // e.g. "2026-09-15"
    val totalQuestions: Int = 10,
    val isCompleted: Boolean = false,
    val scoreAchieved: Int = 0,
    val xpAwarded: Int = 0,
    val completedTimestamp: Long = 0L
)

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val displayName: String,
    val passwordHash: String = "",
    val salt: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey
    val studentId: String,
    val name: String,
    val email: String,
    val standard: Int = 12, // 11 or 12
    val targetExam: String = "MHT-CET 2026",
    val enrollmentDate: Long = System.currentTimeMillis(),
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val xp: Int = 1200,
    val levelNumber: Int = 3,
    val levelTitle: String = "Formula Apprentice",
    val streakDays: Int = 4,
    val totalSolved: Int = 45,
    val totalCorrect: Int = 38,
    val accuracy: Float = 84.4f,
    val studyTimeMinutes: Int = 120,
    val mockTestsTaken: Int = 2,
    val mockTestBestScore: Int = 82, // out of 100
    val mockTestAvgScore: Int = 76,
    val chaptersMastered: Int = 3,
    val totalMistakes: Int = 7,
    val weakTopics: String = "Integration, Conics",
    val strongTopics: String = "Matrices, Logic, Vectors",
    val notes: String = ""
)

@Entity(tableName = "user_login_history")
data class UserLoginHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uid: String,
    val email: String,
    val displayName: String,
    val loginTimestamp: Long = System.currentTimeMillis(),
    val loginMethod: String = "Email & Password", // "Email & Password", "Guest Session", "New Account Registration", "Session Restored"
    val deviceModel: String = "Android Mobile Device",
    val status: String = "Authenticated"
)

