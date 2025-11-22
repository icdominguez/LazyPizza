package com.seno.cart.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.cart.presentation.checkout.components.RadioOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

class OrderCheckoutViewModel: ViewModel() {

    private val _state = MutableStateFlow(OrderCheckoutState())
    val state = _state.asStateFlow()

    init {
        startTimeUpdate()
    }

    fun onAction(action: OrderCheckoutActions) {
        when(action) {
            OrderCheckoutActions.OnEarliestAvailableSelected -> {
                _state.update {
                    it.copy(
                        selectedDeliveryOption = RadioOptions.EARLIEST,
                        showDatePicker = false,
                        showTimePicker = false
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
                _state.update { it.copy(showTimePicker = false) }
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
            is OrderCheckoutActions.OnTimeSelected -> {
                _state.update {
                    it.copy(
                        selectedScheduleTime = action.time,
                        showTimePicker = false
                    )
                }
            }
            else -> Unit
        }
    }

    private fun startTimeUpdate() {
        viewModelScope.launch {
            while (true) {
                _state.update { it.copy(currentTime = LocalTime.now()) }
                delay(60000)
            }
        }
    }
}