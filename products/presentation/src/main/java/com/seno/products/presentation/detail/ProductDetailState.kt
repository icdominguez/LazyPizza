package com.seno.products.presentation.detail

import com.seno.products.domain.model.Product

data class ProductDetailState(
    val isLoading: Boolean = false,
    val selectedPizza: Product.Pizza? = null,
    val listExtraToppings: List<ToppingsUI> = emptyList(),
)

data class ToppingsUI(
    val name: String,
    val image: String,
    val price: Double,
    val quantity: Int
)