package com.example.ui.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ChapterProgressEntity
import com.example.data.model.ChapterMetadata
import com.example.data.model.SyllabusConstants
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*

data class ChapterUiModel(
    val meta: ChapterMetadata,
    val progress: ChapterProgressEntity?
)

data class ChapterListUiState(
    val selectedClassTab: Int = 12, // 11 or 12
    val chaptersXi: List<ChapterUiModel> = emptyList(),
    val chaptersXii: List<ChapterUiModel> = emptyList(),
    val searchQuery: String = ""
)

class ChapterListViewModel(private val repository: MathRepository) : ViewModel() {
    private val _selectedTab = MutableStateFlow(12)
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ChapterListUiState> = combine(
        _selectedTab,
        _searchQuery,
        repository.allChapterProgress
    ) { tab, query, progressList ->
        val progressMap = progressList.associateBy { it.chapterName }

        val xi = SyllabusConstants.STD_XI_CHAPTERS
            .filter { it.name.contains(query, ignoreCase = true) }
            .map { meta -> ChapterUiModel(meta, progressMap[meta.name]) }

        val xii = SyllabusConstants.STD_XII_CHAPTERS
            .filter { it.name.contains(query, ignoreCase = true) }
            .map { meta -> ChapterUiModel(meta, progressMap[meta.name]) }

        ChapterListUiState(
            selectedClassTab = tab,
            chaptersXi = xi,
            chaptersXii = xii,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChapterListUiState()
    )

    fun selectTab(stdClass: Int) {
        _selectedTab.value = stdClass
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
