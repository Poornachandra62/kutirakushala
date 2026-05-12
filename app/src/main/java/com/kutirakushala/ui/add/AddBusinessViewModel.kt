package com.kutirakushala.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutirakushala.data.model.Business
import com.kutirakushala.data.repository.KutiraKushalaRepository
import kotlinx.coroutines.launch

class AddBusinessViewModel(private val repository: KutiraKushalaRepository) : ViewModel() {

    fun saveBusiness(business: Business, onDone: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insertBusiness(business)
            onDone(id)
        }
    }

    fun updateBusiness(business: Business, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.updateBusiness(business)
            onDone()
        }
    }
}
