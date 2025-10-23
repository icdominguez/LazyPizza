package com.seno.cart.presentation

import com.seno.core.presentation.model.CartItemUI

data class CartState(
    var isLoading: Boolean = false,
    val cartItems: List<CartItemUI> = emptyList(),
    val cartId: String = ""
)
