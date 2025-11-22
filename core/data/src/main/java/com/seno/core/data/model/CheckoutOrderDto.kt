package com.seno.core.data.model

import com.google.firebase.firestore.PropertyName
import com.seno.core.domain.checkout.CheckoutOrder

data class CheckoutOrderDto(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = "",
    @get:PropertyName("order_number")
    @set:PropertyName("order_number")
    var orderNumber: String = "#${System.currentTimeMillis().toString().takeLast(5)}",
    @get:PropertyName("pickup_time")
    @set:PropertyName("pickup_time")
    var pickupTime: String = "",
    val items: List<CartDto> = emptyList(),
    @get:PropertyName("total_amount")
    @set:PropertyName("total_amount")
    var totalAmount: Double = 0.0,
    val status: String = "In Progress",
    val timestamp: Long = System.currentTimeMillis(),
)

fun CheckoutOrder.toDto(): CheckoutOrderDto =
    CheckoutOrderDto(
        userId = userId,
        orderNumber = orderNumber,
        pickupTime = pickupTime,
        items = items.map { it.toCharDto() },
        totalAmount = totalAmount,
        status = status,
        timestamp = timestamp,
    )
