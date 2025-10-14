package com.seno.products.presentation.allproducts

sealed interface AllProductsEvent {
    data class Error(val error: String) : AllProductsEvent
}
