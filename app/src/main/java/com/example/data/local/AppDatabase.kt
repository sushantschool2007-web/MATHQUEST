package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.*
import com.example.data.local.entity.*

@Database(
    entities = [
        QuestionEntity::class,
        ChapterProgressEntity::class,
        MistakeEntity::class,
        FormulaEntity::class,
        AchievementEntity::class,
        MockTestResultEntity::class,
        DailyChallengeEntity::class,
        UserProfileEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun chapterDao(): ChapterDao
    abstract fun mistakeDao(): MistakeDao
    abstract fun formulaDao(): FormulaDao
    abstract fun achievementDao(): AchievementDao
    abstract fun mockTestDao(): MockTestDao
    abstract fun dailyChallengeDao(): DailyChallengeDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cet_math_quest.db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
