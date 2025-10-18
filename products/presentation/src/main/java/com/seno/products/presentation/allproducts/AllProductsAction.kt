package com.seno.products.presentation.allproducts

import com.seno.core.domain.product.Product

sealed interface AllProductsAction {
    data class OnProductClicked(val pizzaName: String) : AllProductsAction
    data class OnQueryChange(val newSearchQuery: String) : AllProductsAction
    data class OnProductPlus(val productState: Product): AllProductsAction
    data class OnProductMinus(val productState: Product): AllProductsAction
    data class OnProductDelete(val productState: Product): AllProductsAction
}