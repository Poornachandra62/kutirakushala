package com.kutirakushala.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.kutirakushala.data.model.Business
import com.kutirakushala.data.repository.KutiraKushalaRepository

data class SearchFilter(
    val query: String = "",
    val category: String? = null,
    val location: String = ""
)

class SearchViewModel(private val repository: KutiraKushalaRepository) : ViewModel() {

    private val _filter = MutableLiveData(SearchFilter())
    val filter: LiveData<SearchFilter> = _filter

    val results: LiveData<List<Business>> = _filter.switchMap { f ->
        when {
            f.query.isNotBlank() -> repository.searchBusinesses(f.query)
            else -> repository.filterBusinesses(f.category, f.location.ifBlank { null })
        }
    }

    fun setQuery(q: String) {
        _filter.value = _filter.value?.copy(query = q, category = null, location = "")
    }

    fun setFilter(category: String?, location: String) {
        _filter.value = _filter.value?.copy(query = "", category = category, location = location)
    }

    fun clearFilters() {
        _filter.value = SearchFilter()
    }
}
