package com.seno.products.presentation.allproducts

import com.seno.core.presentation.model.CartItemUI

sealed interface AllProductsAction {
    data class OnProductClicked(val pizzaName: String) : AllProductsAction

    data class OnQueryChange(val newSearchQuery: String) : AllProductsAction

    data class OnProductPlus(val productState: CartItemUI) : AllProductsAction

    data class OnProductMinus(val productState: CartItemUI) : AllProductsAction

    data class OnProductDelete(val productState: CartItemUI) : AllProductsAction
}