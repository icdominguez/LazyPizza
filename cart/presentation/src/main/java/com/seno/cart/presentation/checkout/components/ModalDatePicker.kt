package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.checkout.OrderCheckoutActions
import com.seno.cart.presentation.checkout.OrderCheckoutState
import com.seno.core.presentation.theme.primaryGradient
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ModalDatePicker(
    state: OrderCheckoutState,
    onAction: (OrderCheckoutActions) -> Unit
) {
    // Get today's date at start of day in milliseconds
    val todayMillis = state.todayDate
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = (state.selectedScheduleDate ?: state.todayDate)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
        selectableDates = object : SelectableDates {
            // Disable all dates before today
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis
            }

            // Optional: Disable specific years if needed
            override fun isSelectableYear(year: Int): Boolean {
                return year >= state.todayDate.year
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = { onAction(OrderCheckoutActions.OnDismissDatePicker) },
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onAction(OrderCheckoutActions.OnDateSelected(selectedDate))
                    }
                    onAction(OrderCheckoutActions.OnConfirmDatePicker)
                }
            ) {
                Text("Next")
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(OrderCheckoutActions.OnDismissDatePicker) }) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(),
            headline = {
                Text(
                    text = state.todayDate.format(DateTimeFormatter.ofPattern("dd MMMM")),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerModalPreview() {
    ModalDatePicker(
        state = OrderCheckoutState(),
        onAction = {}
    )
}