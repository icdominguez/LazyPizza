package com.seno.products.presentation.allproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllProductsViewModel(
    private val productsRepository: ProductsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AllProductsState())
    val state = _state.asStateFlow()

    init {
        loadProducts()
    }

    fun onAction(action: AllProductsAction) {
        when(action) {
            is AllProductsAction.OnQueryChange -> {
                val productsFiltered = if(action.newSearchQuery.isNotEmpty()) {
                    _state.value.products.mapValues { (_, products) ->
                        products.filter { product ->
                            product.name.contains(action.newSearchQuery, ignoreCase = true)
                        }
                    }.filterValues { it.isNotEmpty() }
                } else {
                    state.value.products
                }

                _state.update { it.copy(searchQuery = action.newSearchQuery, productsFiltered = productsFiltered) }
            }
            else -> Unit
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            productsRepository.getAllProducts()
                .map { products ->
                    val productsGrouped = products
                        .sortedBy { it.type.ordinal }
                        .groupBy { it.type }

                    var index = 0
                    val headerIndexMap = productsGrouped.entries.associate { (type, items) ->
                        val currentIndex = index
                        index += items.size + 1
                        type to currentIndex
                    }

                    productsGrouped to headerIndexMap
                }
                .collect { (productsGrouped, headerIndexMap) ->
                    _state.update {
                        it.copy(
                            products = productsGrouped,
                            productsFiltered = productsGrouped,
                            headerIndexMap = headerIndexMap,
                            isLoading = false
                        )
                    }
            }
        }
    }
}