package com.seno.cart.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.cart.presentation.checkout.components.RadioOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class OrderCheckoutViewModel: ViewModel() {

    private val _state = MutableStateFlow(OrderCheckoutState())
    val state = _state.asStateFlow()

    companion object {
        val PICKUP_START_TIME: LocalTime = LocalTime.of(10, 15)
        val PICKUP_END_TIME: LocalTime = LocalTime.of(21, 45)
        const val MIN_MINUTES_FROM_NOW = 15L
    }

    init {
        startTimeUpdate()
    }

    private fun startTimeUpdate() {
        viewModelScope.launch {
            while (true) {
                // Add 15 minutes to current time for earliest available pickup
                val earliestTime = LocalTime.now().plusMinutes(MIN_MINUTES_FROM_NOW)
                _state.update { it.copy(currentTime = earliestTime) }
                delay(60000) // Update every minute
            }
        }
    }

    fun onAction(action: OrderCheckoutActions) {
        when(action) {
            OrderCheckoutActions.OnEarliestAvailableSelected -> {
                _state.update {
                    it.copy(
                        selectedDeliveryOption = RadioOptions.EARLIEST,
                        showDatePicker = false,
                        showTimePicker = false,
                        timeValidationError = null
                    )
                }
            }
            OrderCheckoutActions.OnScheduleTimeSelected -> {
                _state.update {
                    it.copy(
                        selectedDeliveryOption = RadioOptions.SCHEDULE,
                        showDatePicker = true
                    )
                }
            }
            OrderCheckoutActions.OnExpandedOrderDetails -> {
                _state.update { it.copy(isOrderDetailsExpanded = !it.isOrderDetailsExpanded) }
            }
            OrderCheckoutActions.OnDismissDatePicker -> {
                _state.update { it.copy(showDatePicker = false) }
            }
            OrderCheckoutActions.OnDismissTimePicker -> {
                _state.update {
                    it.copy(
                        showTimePicker = false,
                        timeValidationError = null
                    )
                }
            }
            OrderCheckoutActions.OnCancelTimePicker -> {
                _state.update {
                    val hasConfirmedTime = it.selectedScheduleTime != null
                    it.copy(
                        showTimePicker = false,
                        timeValidationError = null,
                        selectedDeliveryOption = if (hasConfirmedTime) RadioOptions.SCHEDULE else RadioOptions.EARLIEST,
                        selectedScheduleDate = if (hasConfirmedTime) it.selectedScheduleDate else null,
                        selectedScheduleTime = if (hasConfirmedTime) it.selectedScheduleTime else null
                    )
                }
            }
            OrderCheckoutActions.OnConfirmDatePicker -> {
                _state.update {
                    it.copy(
                        showDatePicker = false,
                        showTimePicker = true
                    )
                }
            }
            is OrderCheckoutActions.OnDateSelected -> {
                _state.update {
                    it.copy(selectedScheduleDate = action.date)
                }
            }
            is OrderCheckoutActions.OnTimeChanged -> {
                val error = validateTime(action.time, _state.value.selectedScheduleDate)
                _state.update {
                    it.copy(timeValidationError = error)
                }
            }
            is OrderCheckoutActions.OnTimeSelected -> {
                val error = validateTime(action.time, _state.value.selectedScheduleDate)
                if (error == null) {
                    _state.update {
                        it.copy(
                            selectedScheduleTime = action.time,
                            showTimePicker = false,
                            timeValidationError = null
                        )
                    }
                }
            }

            else -> Unit
        }
    }

    private fun validateTime(time: LocalTime, selectedDate: LocalDate?): String? {
        // Check if time is within pickup hours
        if (time !in PICKUP_START_TIME..PICKUP_END_TIME) {
            return "Pickup available between $PICKUP_START_TIME and $PICKUP_END_TIME"
        }

        // Check if selected date is today
        val isToday = selectedDate == LocalDate.now()
        if (isToday) {
            val minimumTime = LocalTime.now().plusMinutes(MIN_MINUTES_FROM_NOW)
            if (time < minimumTime) {
                return "Pickup is possible at least 15 minutes from the current time"
            }
        }

        return null
    }
}