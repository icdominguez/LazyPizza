package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seno.cart.presentation.R
import com.seno.cart.presentation.checkout.OrderCheckoutActions
import com.seno.cart.presentation.checkout.OrderCheckoutState
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.outline
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PickUpTimeUI(
    state: OrderCheckoutState,
    onAction: (OrderCheckoutActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.pickup_time),
            style = label_2_semiBold.copy(color = textSecondary)
        )

        Spacer(modifier = Modifier.height(8.dp))

        RadioButtonSingleSelection(
            selectedOption = state.selectedDeliveryOption,
            onSelected = { option ->
                when (option) {
                    RadioOptions.EARLIEST -> onAction(OrderCheckoutActions.OnEarliestAvailableSelected)
                    RadioOptions.SCHEDULE -> onAction(OrderCheckoutActions.OnScheduleTimeSelected)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (state.selectedDeliveryOption == RadioOptions.EARLIEST) {
                    stringResource(R.string.earliest_pickup_time)
                } else {
                    stringResource(R.string.scheduled_time)
                },
                style = MaterialTheme.typography.labelSmall.copy(color = textSecondary)
            )

            Text(
                text = state.displayPickupTime,
                style = label_2_semiBold.copy(color = textPrimary)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp, color = outline)
    }

    if (state.showDatePicker) {
        ModalDatePicker(
            initialSelectedDateMillis = state.selectedScheduleDateMillis ?: state.todayDateMillis,
            minSelectableDateMillis = state.minSelectableDateMillis,
            onDateSelected = { date ->
                onAction(OrderCheckoutActions.OnDateSelected(date))
            },
            onConfirmDatePicker = {
                onAction(OrderCheckoutActions.OnConfirmDatePicker)
            },
            onDismiss = {
                onAction(OrderCheckoutActions.OnDismissDatePicker)
            }
        )
    }

    if (state.showTimePicker) {
        InputTimePicker(
            onConfirm = { selectedTime ->
                onAction(OrderCheckoutActions.OnTimeSelected(selectedTime))
            },
            onDismiss = { onAction(OrderCheckoutActions.OnDismissTimePicker) },
            onCancel = { onAction(OrderCheckoutActions.OnCancelTimePicker) },
            onTimeChanged = { time ->
                onAction(OrderCheckoutActions.OnTimeChanged(time))
            },
            initialTime = state.selectedScheduleTime,
            validationError = state.timeValidationError?.asString()
        )
    }
}