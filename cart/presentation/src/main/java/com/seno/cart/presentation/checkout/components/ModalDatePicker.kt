package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.checkout.OrderCheckoutState
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_semiBold
import com.seno.core.presentation.theme.title_3
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ModalDatePicker(
    state: OrderCheckoutState,
    onDismiss: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onConfirmDatePicker: () -> Unit = {}
) {
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

    val displayDate = datePickerState.selectedDateMillis?.let { millis ->
        Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("MMMM dd"))
    } ?: state.todayDate.format(DateTimeFormatter.ofPattern("MMMM dd"))

    DatePickerDialog(
        shape = RoundedCornerShape(12.dp),
        onDismissRequest = { onDismiss() },
        confirmButton = {
            LazyPizzaPrimaryButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(selectedDate)

                    }
                    onConfirmDatePicker()
                },
                buttonText = "Ok"
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = "Cancel",
                    style = title_3.copy(color = primary)
                )
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "Select date".uppercase(),
                    modifier = Modifier.padding(start = 24.dp, top = 48.dp),
                    style = label_2_semiBold.copy(color = textSecondary)
                )
            },
            headline = {
                Text(
                    text = displayDate,
                    modifier = Modifier.padding(start = 24.dp, bottom = 24.dp),
                    style = title_1_semiBold.copy(color = textPrimary)
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
        onDismiss = {},
        onDateSelected = {},
        onConfirmDatePicker = {}
    )
}