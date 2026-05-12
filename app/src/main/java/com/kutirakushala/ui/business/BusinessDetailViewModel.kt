package com.kutirakushala.ui.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutirakushala.data.model.CapacityStatus
import com.kutirakushala.data.repository.KutiraKushalaRepository
import kotlinx.coroutines.launch

class BusinessDetailViewModel(private val repository: KutiraKushalaRepository) : ViewModel() {

    fun getBusinessWithProducts(id: Int) = repository.getBusinessWithProducts(id)

    fun getProducts(businessId: Int) = repository.getProductsForBusiness(businessId)

    fun updateCapacity(id: Int, units: Int, status: CapacityStatus) {
        viewModelScope.launch { repository.updateCapacity(id, units, status) }
    }
}
