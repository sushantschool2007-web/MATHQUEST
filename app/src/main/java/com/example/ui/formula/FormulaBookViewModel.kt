package com.example.ui.formula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.FormulaEntity
import com.example.data.repository.MathRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class FormulaFilter {
    ALL,
    CLASS_11,
    CLASS_12,
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
        val filtered = all.filter { f ->
            val matchesQuery = query.isBlank() ||
                f.title.contains(query, ignoreCase = true) ||
                f.chapter.contains(query, ignoreCase = true) ||
                f.topic.contains(query, ignoreCase = true) ||
                f.formula.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                FormulaFilter.ALL -> true
                FormulaFilter.CLASS_11 -> f.stdClass == 11
                FormulaFilter.CLASS_12 -> f.stdClass == 12
                FormulaFilter.BOOKMARKED -> f.isBookmarked
            }

            matchesQuery && matchesFilter
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
