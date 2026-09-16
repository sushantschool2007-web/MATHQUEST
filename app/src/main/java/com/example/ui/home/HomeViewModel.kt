package com.example.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferences
import com.example.data.local.entity.ChapterProgressEntity
import com.example.data.model.GamificationConfig
import com.example.data.model.LevelInfo
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class HomeUiState(
    val userPrefs: UserPreferences? = null,
    val levelInfo: LevelInfo = GamificationConfig.LEVELS.first(),
    val nextLevelInfo: LevelInfo? = GamificationConfig.LEVELS.getOrNull(1),
    val levelProgress: Float = 0f,
    val activeChapterProgress: ChapterProgressEntity? = null,
    val totalChapters: Int = 26,
    val completedChapters: Int = 0,
    val overallProgress: Float = 0f,
    val unresolvedMistakeCount: Int = 0,
    val dailyChallengeCompleted: Boolean = false
)

class HomeViewModel(private val repository: MathRepository) : ViewModel() {

    private val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val uiState: StateFlow<HomeUiState> = combine(
        repository.userPreferencesFlow,
        repository.allChapterProgress,
        repository.unresolvedMistakeCount,
        repository.getDailyChallenge(todayKey)
    ) { prefs, chapters, mistakeCount, dailyChallenge ->
        val levelInfo = GamificationConfig.getLevelForXp(prefs.totalXp)
        val nextLevel = GamificationConfig.LEVELS.firstOrNull { it.levelNumber == levelInfo.levelNumber + 1 }
        val progressInLevel = if (nextLevel != null) {
            val range = (nextLevel.minXP - levelInfo.minXP).toFloat()
            if (range > 0) ((prefs.totalXp - levelInfo.minXP).toFloat() / range).coerceIn(0f, 1f) else 1f
        } else 1f

        val activeChapter = chapters.firstOrNull { it.chapterName == prefs.activeChapter } ?: chapters.firstOrNull()
        val completedCount = chapters.count { it.mastery >= 80 }
        val overall = if (chapters.isNotEmpty()) {
            chapters.map { it.mastery }.average().toFloat() / 100f
        } else 0f

        HomeUiState(
            userPrefs = prefs,
            levelInfo = levelInfo,
            nextLevelInfo = nextLevel,
            levelProgress = progressInLevel,
            activeChapterProgress = activeChapter,
            totalChapters = chapters.size.coerceAtLeast(26),
            completedChapters = completedCount,
            overallProgress = overall,
            unresolvedMistakeCount = mistakeCount,
            dailyChallengeCompleted = dailyChallenge?.isCompleted ?: false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
}
