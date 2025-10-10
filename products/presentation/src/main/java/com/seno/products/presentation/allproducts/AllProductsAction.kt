package com.seno.products.presentation.allproducts

sealed interface AllProductsAction {
    data class OnProductClicked(val pizzaName: String) : AllProductsAction
}