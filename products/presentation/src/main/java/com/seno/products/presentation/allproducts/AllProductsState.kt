package com.seno.products.presentation.allproducts

import com.seno.core.domain.product.ProductType
import com.seno.core.presentation.model.CartItemUI

data class AllProductsState(
    val isLoading: Boolean = true,
    val isCreateNewCart: Boolean = false,
    val products: Map<ProductType, List<CartItemUI>> = emptyMap(),
    val headerIndexMap: Map<ProductType, Int> = emptyMap(),
    val productsFiltered: Map<ProductType, List<CartItemUI>> = emptyMap(),
    val searchQuery: String = "",
    val isLoggedIn: Boolean = false,
    val showLogoutDialog: Boolean = false,
)
