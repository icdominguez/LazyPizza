package com.seno.products.presentation.detail

sealed interface ProductDetailAction {
    data class OnToppingPlus(val toppingsUI: ToppingsUI) : ProductDetailAction

    data class OnToppingMinus(val toppingsUI: ToppingsUI) : ProductDetailAction

    data object OnAddToCartButtonClick : ProductDetailAction

    data object OnBackClick : ProductDetailAction
}
