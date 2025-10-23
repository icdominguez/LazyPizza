package com.seno.products.presentation.detail

sealed interface ProductDetailEvent {
    data class Error(val error: String): ProductDetailEvent
    data object OnCartSuccessfullySaved: ProductDetailEvent
}