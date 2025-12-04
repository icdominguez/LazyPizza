package com.seno.history.presentation

import com.seno.history.domain.model.HistoryOrder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class HistoryState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = true,
    val orderItems: List<OrderItem> = emptyList()
)

fun HistoryOrder.toOrderItem(): OrderItem {
    return OrderItem(
        id = orderNumber,
        date = pickupTime,
        items = items,
        totalPrice = totalAmount,
        status = getOrderStatusFromPickupTime(pickupTime),
    )
}

private fun getOrderStatusFromPickupTime(pickUpTime: Long): OrderStatus {
    val now = LocalDateTime.now()

    val pickUpDateTime = Instant.ofEpochMilli(pickUpTime)
        .atZone(ZoneId.of("UTC"))
        .toLocalDateTime()

    return if (now.isBefore(pickUpDateTime)) {
        OrderStatus.IN_PROGRESS
    } else {
        OrderStatus.COMPLETED
    }
}
