package com.seno.products.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductDetailViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val pizzaName = savedStateHandle.get<String>(PIZZA_NAME) ?: ""

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    companion object {
        private const val PIZZA_NAME = "pizzaName"
    }
}