package com.example.ui.formula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.FormulaEntity
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class FormulaFilter {
    ALL,
    TAG_MHT_CET,
    TAG_JEE,
    CLASS_12,
    CLASS_11,
    BOOKMARKED
}

data class FormulaBookUiState(
    val formulas: List<FormulaEntity> = emptyList(),
    val searchQuery: String = "",
    val activeFilter: FormulaFilter = FormulaFilter.ALL
)

class FormulaBookViewModel(private val repository: MathRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _filter = MutableStateFlow(FormulaFilter.ALL)

    val uiState: StateFlow<FormulaBookUiState> = combine(
        _searchQuery,
        _filter,
        repository.allFormulas
    ) { query, filter, all ->
        val cleanQuery = query.trim()
        val isMhtCetTagSearch = cleanQuery.contains("#MHT-CET", ignoreCase = true) || cleanQuery.contains("#CET", ignoreCase = true)
        val isJeeTagSearch = cleanQuery.contains("#JEE", ignoreCase = true)

        val strippedQuery = cleanQuery
            .replace("#MHT-CET", "", ignoreCase = true)
            .replace("#CET", "", ignoreCase = true)
            .replace("#JEE", "", ignoreCase = true)
            .trim()

        val filtered = all.filter { f ->
            val matchesQuery = strippedQuery.isBlank() ||
                f.title.contains(strippedQuery, ignoreCase = true) ||
                f.chapter.contains(strippedQuery, ignoreCase = true) ||
                f.topic.contains(strippedQuery, ignoreCase = true) ||
                f.formula.contains(strippedQuery, ignoreCase = true) ||
                f.whenToUse.contains(strippedQuery, ignoreCase = true) ||
                f.shortcut.contains(strippedQuery, ignoreCase = true)

            val matchesTag = when {
                isMhtCetTagSearch -> f.shortcut.isNotBlank() || f.stdClass in 11..12
                isJeeTagSearch -> f.stdClass == 12 || f.formula.contains("\\int") || f.formula.contains("\\matrix")
                else -> true
            }

            val matchesFilter = when (filter) {
                FormulaFilter.ALL -> true
                FormulaFilter.TAG_MHT_CET -> f.shortcut.isNotBlank() || f.stdClass in 11..12
                FormulaFilter.TAG_JEE -> f.stdClass == 12 || f.formula.contains("\\int")
                FormulaFilter.CLASS_11 -> f.stdClass == 11
                FormulaFilter.CLASS_12 -> f.stdClass == 12
                FormulaFilter.BOOKMARKED -> f.isBookmarked
            }

            matchesQuery && matchesTag && matchesFilter
        }

        FormulaBookUiState(
            formulas = filtered,
            searchQuery = query,
            activeFilter = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FormulaBookUiState()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: FormulaFilter) {
        _filter.value = filter
    }

    fun toggleBookmark(formula: FormulaEntity) {
        viewModelScope.launch {
            repository.toggleFormulaBookmark(formula.id, formula.isBookmarked)
        }
    }
}
