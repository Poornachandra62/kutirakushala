package com.kutirakushala.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutirakushala.data.model.Business
import com.kutirakushala.data.repository.KutiraKushalaRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: KutiraKushalaRepository) : ViewModel() {
    val businesses = repository.allBusinesses

    fun init() {
        viewModelScope.launch { repository.seedSampleDataIfEmpty() }
    }

    fun deleteBusiness(business: Business) {
        viewModelScope.launch { repository.deleteBusiness(business) }
    }
}
