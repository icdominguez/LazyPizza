package com.seno.history.presentation

data class OrderItem(
    val id: String = "",
    val date: String = "",
    val items: List<String> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.IN_PROGRESS,
)
