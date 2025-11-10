package com.seno.history.presentation

import androidx.compose.ui.graphics.Color

enum class OrderStatus(val description: String, val color: Color) {
    IN_PROGRESS("In Progress", Color(0xFFF9A825)),
    COMPLETED("Completed", Color(0xFF2E7D32)),
}
