package com.seno.history.presentation

import com.seno.history.domain.model.HistoryOrder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Locale

data class HistoryState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = true,
    val orderItems: List<OrderItem> = emptyList()
)

fun HistoryOrder.toOrderItem(): OrderItem {
    val dateString = pickupTime
    return OrderItem(
        id = orderNumber,
        date = dateString,
        items = items,
        totalPrice = totalAmount,
        status = getOrderStatusFromPickupTime(pickupTime),
    )
}

private fun getOrderStatusFromPickupTime(pickupTimeString: String): OrderStatus {
    val inputFormatter = DateTimeFormatter.ofPattern(
        "MMMM d, HH:mm",
        Locale.getDefault(),
    )

    val now = LocalDateTime.now()

    val parsedDate = inputFormatter.parse(pickupTimeString)
    val targetDateTime = LocalDateTime.of(
        now.year,
        parsedDate.get(ChronoField.MONTH_OF_YEAR),
        parsedDate.get(ChronoField.DAY_OF_MONTH),
        parsedDate.get(ChronoField.HOUR_OF_DAY),
        parsedDate.get(ChronoField.MINUTE_OF_HOUR),
    )

    return if (now.isBefore(targetDateTime)) {
        OrderStatus.IN_PROGRESS
    } else {
        OrderStatus.COMPLETED
    }
}
