package com.seno.history.domain.model

data class HistoryOrder(
    val id: String,
    val userId: String = "",
    val orderNumber: String = "#${System.currentTimeMillis().toString().takeLast(5)}",
    val pickupTime: String = "",
    val items: List<String> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: String = "In Progress",
    val timestamp: Long = System.currentTimeMillis(),
)
