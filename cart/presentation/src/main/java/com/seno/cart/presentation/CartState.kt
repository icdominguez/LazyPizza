package com.seno.cart.presentation

import com.seno.core.presentation.model.CartItemUI

data class CartState(
    val isLoading: Boolean = false,
    val isUpdatingCart: Boolean = false,
    val cartItems: List<CartItemUI> = emptyList(),
    val recommendedItems: List<CartItemUI> = emptyList(),
    val cartId: String = ""
)