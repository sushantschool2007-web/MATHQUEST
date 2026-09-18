package com.example.ui.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ChapterProgressEntity
import com.example.data.model.ComprehensiveChapter
import com.example.data.model.MathematicsRepositoryCatalog
import com.example.data.model.ValidationReport
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*

data class ComprehensiveChapterUiModel(
    val chapter: ComprehensiveChapter,
    val progress: ChapterProgressEntity?
)

enum class SyllabusViewMode(val title: String) {
    STD_11_PART_1("Std 11 Part 1"),
    STD_11_PART_2("Std 11 Part 2"),
    STD_12_PART_1("Std 12 Part 1"),
    STD_12_PART_2("Std 12 Part 2"),
    MHT_CET("MHT-CET"),
    JEE_MAIN("JEE Main")
}

data class ChapterListUiState(
    val selectedMode: SyllabusViewMode = SyllabusViewMode.STD_12_PART_1,
    val chapters: List<ComprehensiveChapterUiModel> = emptyList(),
    val searchQuery: String = "",
    val validationReport: ValidationReport = MathematicsRepositoryCatalog.validateSyllabusHierarchy(),
    val showValidationDialog: Boolean = false
)

class ChapterListViewModel(private val repository: MathRepository) : ViewModel() {
    private val _selectedMode = MutableStateFlow(SyllabusViewMode.STD_12_PART_1)
    private val _searchQuery = MutableStateFlow("")
    private val _showValidation = MutableStateFlow(false)

    val uiState: StateFlow<ChapterListUiState> = combine(
        _selectedMode,
        _searchQuery,
        _showValidation,
        repository.allChapterProgress
    ) { mode, query, showVal, progressList ->
        val progressMap = progressList.associateBy { it.chapterName.lowercase() }

        val rawChapters = when (mode) {
            SyllabusViewMode.STD_11_PART_1 -> MathematicsRepositoryCatalog.getChaptersByStandardAndPart(11, 1)
            SyllabusViewMode.STD_11_PART_2 -> MathematicsRepositoryCatalog.getChaptersByStandardAndPart(11, 2)
            SyllabusViewMode.STD_12_PART_1 -> MathematicsRepositoryCatalog.getChaptersByStandardAndPart(12, 1)
            SyllabusViewMode.STD_12_PART_2 -> MathematicsRepositoryCatalog.getChaptersByStandardAndPart(12, 2)
            SyllabusViewMode.MHT_CET -> MathematicsRepositoryCatalog.getMhtCetChapters()
            SyllabusViewMode.JEE_MAIN -> MathematicsRepositoryCatalog.getJeeMainChapters()
        }

        val filtered = if (query.isBlank()) {
            rawChapters
        } else {
            rawChapters.filter { ch ->
                ch.name.contains(query, ignoreCase = true) ||
                ch.subtopics.any { it.name.contains(query, ignoreCase = true) } ||
                ch.importantFormulas.any { it.formulaName.contains(query, ignoreCase = true) }
            }
        }

        val models = filtered.map { ch ->
            val prog = progressMap[ch.name.lowercase()]
            ComprehensiveChapterUiModel(chapter = ch, progress = prog)
        }

        ChapterListUiState(
            selectedMode = mode,
            chapters = models,
            searchQuery = query,
            validationReport = MathematicsRepositoryCatalog.validateSyllabusHierarchy(),
            showValidationDialog = showVal
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChapterListUiState()
    )

    fun selectMode(mode: SyllabusViewMode) {
        _selectedMode.value = mode
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleValidationDialog(show: Boolean) {
        _showValidation.value = show
    }
}
