package com.seno.cart.presentation.checkout

import java.time.LocalDate
import java.time.LocalTime

sealed interface OrderCheckoutActions {
    data object OnNavigateUp: OrderCheckoutActions
    data object OnPlaceOrder: OrderCheckoutActions
    data object OnDismissDatePicker : OrderCheckoutActions
    data object OnDismissTimePicker : OrderCheckoutActions
    data object OnConfirmDatePicker : OrderCheckoutActions
    data object OnScheduleTimeSelected: OrderCheckoutActions
    data object OnExpandedOrderDetails: OrderCheckoutActions
    data object OnEarliestAvailableSelected: OrderCheckoutActions
    data class OnDateSelected(val date: LocalDate) : OrderCheckoutActions
    data class OnTimeSelected(val time: LocalTime) : OrderCheckoutActions
}