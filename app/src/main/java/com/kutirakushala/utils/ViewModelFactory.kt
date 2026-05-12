package com.kutirakushala.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kutirakushala.data.repository.KutiraKushalaRepository
import com.kutirakushala.ui.add.AddBusinessViewModel
import com.kutirakushala.ui.business.BusinessDetailViewModel
import com.kutirakushala.ui.home.HomeViewModel
import com.kutirakushala.ui.product.AddProductViewModel
import com.kutirakushala.ui.search.SearchViewModel

class ViewModelFactory(private val repository: KutiraKushalaRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
        modelClass.isAssignableFrom(BusinessDetailViewModel::class.java) -> BusinessDetailViewModel(repository) as T
        modelClass.isAssignableFrom(AddBusinessViewModel::class.java) -> AddBusinessViewModel(repository) as T
        modelClass.isAssignableFrom(AddProductViewModel::class.java) -> AddProductViewModel(repository) as T
        modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
