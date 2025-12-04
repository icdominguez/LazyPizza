package com.seno.cart.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.cart.domain.CheckoutRepository
import com.seno.cart.presentation.R
import com.seno.cart.presentation.checkout.components.RadioOptions
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.checkout.CheckoutOrder
import com.seno.core.domain.repository.CoreRepository
import com.seno.core.domain.userdata.UserData
import com.seno.core.presentation.utils.SnackbarAction
import com.seno.core.presentation.utils.SnackbarController
import com.seno.core.presentation.utils.SnackbarEvent
import com.seno.core.presentation.utils.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.minutes

class OrderCheckoutViewModel(
    private val userData: UserData,
    private val checkoutRepository: CheckoutRepository,
    private val coreRepository: CoreRepository
) : ViewModel() {
    private val _state = MutableStateFlow(OrderCheckoutState())
    val state = _state.asStateFlow()

    companion object {
        val PICKUP_START_TIME: LocalTime = LocalTime.of(10, 15)
        val PICKUP_END_TIME: LocalTime = LocalTime.of(21, 45)
        const val MIN_MINUTES_FROM_NOW = 15L
    }

    init {
        initializeDates()
        startTimeUpdate()
        userData
            .getUserId()
            .onEach { userId ->
                _state.update { it.copy(isUserLoggedIn = userId != null) }
            }.launchIn(viewModelScope)
    }

    private fun initializeDates() {
        val todayMillis = LocalDate
            .now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        _state.update {
            it.copy(
                todayDateMillis = todayMillis,
                minSelectableDateMillis = todayMillis,
            )
        }
    }

    private fun startTimeUpdate() {
        viewModelScope.launch {
            while (true) {
                val earliestTime = LocalTime.now().plusMinutes(MIN_MINUTES_FROM_NOW)
                _state.update {
                    it.copy(
                        currentTime = earliestTime,
                        displayPickupTime = formatPickupTime(earliestTime, it),
                    )
                }
                delay(1.minutes)
            }
        }
    }

    private fun formatPickupTime(
        currentTime: LocalTime,
        state: OrderCheckoutState
    ): String =
        if (state.selectedDeliveryOption == RadioOptions.EARLIEST) {
            currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            formatScheduledPickup(
                state.selectedScheduleDateMillis,
                state.selectedScheduleTime,
                state.todayDateMillis,
            )
        }

    private fun formatScheduledPickup(
        selectedDateMillis: Long?,
        selectedTime: LocalTime?,
        todayDateMillis: Long
    ): String {
        if (selectedDateMillis == null) return "Select Date"

        val selectedDate = Instant
            .ofEpochMilli(selectedDateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val todayDate = Instant
            .ofEpochMilli(todayDateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val isToday = selectedDate == todayDate

        return when {
            selectedTime == null -> selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM"))
            isToday -> selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            else ->
                "${
                    selectedDate.format(
                        DateTimeFormatter.ofPattern("MMMM dd"),
                    )
                }, ${selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        }
    }

    private fun updateDisplayStrings() {
        _state.update {
            it.copy(
                displayPickupTime = formatPickupTime(it.currentTime, it),
            )
        }
    }

    fun onAction(action: OrderCheckoutActions) {
        when (action) {
            OrderCheckoutActions.OnEarliestAvailableSelected -> {
                _state.update {
                    it.copy(
                        selectedDeliveryOption = RadioOptions.EARLIEST,
                        showDatePicker = false,
                        showTimePicker = false,
                        timeValidationError = null,
                    )
                }
                updateDisplayStrings()
            }

            OrderCheckoutActions.OnScheduleTimeSelected -> {
                _state.update {
                    it.copy(
                        selectedDeliveryOption = RadioOptions.SCHEDULE,
                        showDatePicker = true,
                    )
                }
                updateDisplayStrings()
            }

            OrderCheckoutActions.OnExpandedOrderDetails -> {
                _state.update { it.copy(isOrderDetailsExpanded = !it.isOrderDetailsExpanded) }
            }

            OrderCheckoutActions.OnCancelPickupTimePicker -> {
                _state.update {
                    val hasConfirmedTime = it.selectedScheduleTime != null
                    it.copy(
                        showDatePicker = false,
                        showTimePicker = false,
                        timeValidationError = null,
                        selectedDeliveryOption = if (hasConfirmedTime) RadioOptions.SCHEDULE else RadioOptions.EARLIEST,
                        selectedScheduleDateMillis = if (hasConfirmedTime) it.selectedScheduleDateMillis else null,
                        selectedScheduleTime = if (hasConfirmedTime) it.selectedScheduleTime else null,
                    )
                }
                updateDisplayStrings()
            }

            OrderCheckoutActions.OnConfirmDatePicker -> {
                _state.update {
                    it.copy(
                        showDatePicker = false,
                        showTimePicker = true,
                    )
                }
            }

            is OrderCheckoutActions.OnDateSelected -> {
                val dateMillis = action.date
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                _state.update {
                    it.copy(selectedScheduleDateMillis = dateMillis)
                }
                updateDisplayStrings()
            }

            is OrderCheckoutActions.OnTimeChanged -> {
                val selectedDate = _state.value.selectedScheduleDateMillis?.let {
                    Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                val error = validateTime(action.time, selectedDate)
                _state.update {
                    it.copy(timeValidationError = error)
                }
            }

            is OrderCheckoutActions.OnTimeSelected -> {
                val selectedDate = _state.value.selectedScheduleDateMillis?.let {
                    Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                val error = validateTime(action.time, selectedDate)
                if (error == null) {
                    _state.update {
                        it.copy(
                            selectedScheduleTime = action.time,
                            showTimePicker = false,
                            timeValidationError = null,
                        )
                    }
                    updateDisplayStrings()
                }
            }

            is OrderCheckoutActions.OnCommentTextChange -> {
                _state.update {
                    it.copy(comment = action.comment)
                }
            }

            is OrderCheckoutActions.OnPlaceOrderClick -> placeOrder(action.totalPrice)

            else -> Unit
        }
    }

    private fun placeOrder(totalPrice: Double) {
        viewModelScope.launch {
            val userId = userData.getUserId().first() ?: return@launch
            val cartId = userData.getCartId().first() ?: return@launch
            val cartResult = coreRepository.getCart(cartId).first()
            if (cartResult is FirebaseResult.Error) {
                SnackbarController.sendEvent(
                    SnackbarEvent("Place order error, please try again", SnackbarAction("OK") {}),
                )
                return@launch
            }
            val items = (cartResult as FirebaseResult.Success).data

            _state.update {
                it.copy(
                    isPlaceOrderLoading = true,
                )
            }

            val pickupTimeMillis = when (_state.value.selectedDeliveryOption) {
                RadioOptions.EARLIEST -> {
                    val todayDate = LocalDate.now()
                    LocalDateTime
                        .of(todayDate, _state.value.currentTime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                }

                RadioOptions.SCHEDULE -> {
                    val selectedDateMillis = _state.value.selectedScheduleDateMillis
                    val selectedTime = _state.value.selectedScheduleTime

                    if (selectedDateMillis == null || selectedTime == null) {
                        val todayDate = LocalDate.now()
                        LocalDateTime
                            .of(todayDate, _state.value.currentTime)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    } else {
                        val selectedLocalDate = Instant
                            .ofEpochMilli(selectedDateMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        LocalDateTime
                            .of(selectedLocalDate, selectedTime)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    }
                }
            }

            val result = checkoutRepository.sendOrder(
                CheckoutOrder(
                    userId = userId,
                    pickupTime = pickupTimeMillis,
                    items = items,
                    totalAmount = totalPrice
                        .toBigDecimal()
                        .setScale(2, RoundingMode.HALF_UP)
                        .toDouble(),
                ),
            )

            when (result) {
                is FirebaseResult.Error -> {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            "Place order error, please try again",
                            SnackbarAction("OK") {},
                        ),
                    )
                }

                is FirebaseResult.Success -> {
                    _state.update {
                        it.copy(
                            placeOrderSuccess = true,
                            placeOrderNumber = result.data,
                            placeOrderPickupTime = pickupTimeMillis,
                        )
                    }
                }
            }
            _state.update {
                it.copy(
                    isPlaceOrderLoading = false,
                )
            }
        }
    }

    private fun validateTime(
        time: LocalTime,
        selectedDate: LocalDate?
    ): UiText? {
        if (time !in PICKUP_START_TIME..PICKUP_END_TIME) {
            return UiText.StringResource(
                id = R.string.pickup_time_range_error,
                args = arrayOf(
                    PICKUP_START_TIME.toString(),
                    PICKUP_END_TIME.toString(),
                ),
            )
        }
        val isToday = selectedDate == LocalDate.now()
        if (isToday) {
            val minimumTime = LocalTime.now().plusMinutes(MIN_MINUTES_FROM_NOW)
            if (time < minimumTime) {
                return UiText.StringResource(
                    id = R.string.pickup_time_minimum_error,
                    args = arrayOf(MIN_MINUTES_FROM_NOW.toString()),
                )
            }
        }
        return null
    }
}
