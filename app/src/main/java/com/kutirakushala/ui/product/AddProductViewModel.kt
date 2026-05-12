package com.kutirakushala.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutirakushala.data.model.Product
import com.kutirakushala.data.repository.KutiraKushalaRepository
import kotlinx.coroutines.launch

class AddProductViewModel(private val repository: KutiraKushalaRepository) : ViewModel() {

    fun saveProduct(product: Product, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.insertProduct(product)
            onDone()
        }
    }

    fun updateProduct(product: Product, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.updateProduct(product)
            onDone()
        }
    }

    fun deleteProduct(product: Product, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            onDone()
        }
    }
}
