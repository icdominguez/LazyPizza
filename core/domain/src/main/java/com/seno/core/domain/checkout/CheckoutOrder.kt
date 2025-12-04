package com.seno.core.domain.checkout

import com.seno.core.domain.cart.CartItem

data class CheckoutOrder(
    val userId: String = "",
    val orderNumber: String = "#${System.currentTimeMillis().toString().takeLast(5)}",
    val pickupTime: Long = 0L,
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: String = "In Progress",
    val timestamp: Long = System.currentTimeMillis(),
)
