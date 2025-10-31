package com.seno.products.presentation.detail

import com.seno.core.domain.product.Product
import com.seno.core.domain.product.ProductType

data class ProductDetailState(
    val isLoading: Boolean = false,
    val isUpdatingCart: Boolean = false,
    val selectedPizza: Product.Pizza? = null,
    val listExtraToppings: List<ToppingsUI> = emptyList(),
)

data class ToppingsUI(
    val id: String,
    val type: ProductType = ProductType.EXTRA_TOPPING,
    val name: String,
    val image: String,
    val price: Double,
    val quantity: Int
)
