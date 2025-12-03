package com.seno.cart.presentation.checkout

import com.seno.cart.presentation.checkout.components.RadioOptions
import com.seno.core.presentation.utils.UiText
import java.time.LocalTime

data class OrderCheckoutState(
    val selectedDeliveryOption: RadioOptions = RadioOptions.EARLIEST,
    val isOrderDetailsExpanded: Boolean = false,
    val todayDateMillis: Long = 0L,
    val selectedScheduleDateMillis: Long? = null,
    val selectedScheduleTime: LocalTime? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val currentTime: LocalTime = LocalTime.now(),
    val timeValidationError: UiText? = null,
    val minSelectableDateMillis: Long = 0L,
    val displayPickupTime: String = "",
    val comment: String = "",
    val isUserLoggedIn: Boolean = false,
    val isPlaceOrderLoading: Boolean = false,
    val placeOrderSuccess: Boolean = false,
    val placeOrderNumber: String = "",
    val placeOrderPickupTime: Long = 0L
)
