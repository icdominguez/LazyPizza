package com.seno.history.presentation

import java.util.UUID

data class OrderItem(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val items: List<String> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.IN_PROGRESS,
)
