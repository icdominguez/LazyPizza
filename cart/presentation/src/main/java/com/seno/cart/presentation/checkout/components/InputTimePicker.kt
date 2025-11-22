package com.seno.cart.presentation.checkout.components

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTimePicker(
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    initialTime: LocalTime? = null
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = initialTime?.hour ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = initialTime?.minute ?: currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val selectedTime = LocalTime.of(
                    timePickerState.hour,
                    timePickerState.minute
                )
                onConfirm(selectedTime)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        title = {
            Text("Select Time")
        },
    ) {
        TimeInput(state = timePickerState)
    }
}