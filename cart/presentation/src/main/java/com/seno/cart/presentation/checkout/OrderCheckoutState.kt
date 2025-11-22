package com.seno.cart.presentation.checkout

import com.seno.cart.presentation.checkout.components.RadioOptions
import java.time.LocalDate
import java.time.LocalTime

data class OrderCheckoutState(
    val selectedDeliveryOption: RadioOptions = RadioOptions.EARLIEST,
    val isOrderDetailsExpanded: Boolean = false,
    val todayDate: LocalDate = LocalDate.now(),
    val selectedScheduleDate: LocalDate? = null,
    val selectedScheduleTime: LocalTime? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val currentTime: LocalTime = LocalTime.now()
)
