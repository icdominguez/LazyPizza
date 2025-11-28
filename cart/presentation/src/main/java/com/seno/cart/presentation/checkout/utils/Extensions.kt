package com.seno.cart.presentation.checkout.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toPlaceOrderPickupTime(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("MMMM dd, HH:mm", Locale.getDefault())
    return formatter.format(date)
}
