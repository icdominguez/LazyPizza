package com.seno.cart.presentation.checkout

import java.time.LocalDate
import java.time.LocalTime

sealed interface OrderCheckoutActions {
    data object OnCancelPickupTimePicker : OrderCheckoutActions

    data object OnConfirmDatePicker : OrderCheckoutActions

    data object OnScheduleTimeSelected : OrderCheckoutActions

    data object OnExpandedOrderDetails : OrderCheckoutActions

    data object OnEarliestAvailableSelected : OrderCheckoutActions

    data class OnDateSelected(val date: LocalDate) : OrderCheckoutActions

    data class OnTimeSelected(val time: LocalTime) : OrderCheckoutActions

    data class OnTimeChanged(val time: LocalTime) : OrderCheckoutActions

    data class OnCommentTextChange(val comment: String) : OrderCheckoutActions

    data object OnBackClick : OrderCheckoutActions

    data object OnLoginClick : OrderCheckoutActions

    data object OnMainMenuClick : OrderCheckoutActions

    data class OnPlaceOrderClick(val totalPrice: Double) : OrderCheckoutActions
}
