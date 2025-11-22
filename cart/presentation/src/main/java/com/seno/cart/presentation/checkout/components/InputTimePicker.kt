package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.surfaceHigher
import com.seno.core.presentation.theme.surfaceHighest
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_3
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTimePicker(
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onTimeChanged: (LocalTime) -> Unit,
    initialTime: LocalTime? = null,
    validationError: String? = null
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = initialTime?.hour ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = initialTime?.minute ?: currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
        onTimeChanged(selectedTime)
    }

    TimePickerDialog(
        containerColor = surfaceHighest,
        onDismissRequest = onDismiss,
        confirmButton = {
            // reason why i didn't use lazy pizza primary button is we didn't implement enabled parameter.
            Button(
                onClick = {
                    if (validationError == null) {
                        val selectedTime = LocalTime.of(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        onConfirm(selectedTime)
                    }
                },
                enabled = validationError == null
            ) {
                Text(
                    text = "OK",
                    style = title_3.copy(color = textOnPrimary)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text(
                    text = "Cancel",
                    style = title_3.copy(color = primary)
                )
            }
        },
        title = {
            Text(
                text = "Select Time".uppercase(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                style = label_2_semiBold.copy(color = textSecondary)
            )
        },
    ) {
        Column {
            TimeInput(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    timeSelectorSelectedContainerColor = surfaceHigher,
                    timeSelectorUnselectedContainerColor = surfaceHighest,
                    timeSelectorSelectedContentColor = Color(0xFF001945),
                    timeSelectorUnselectedContentColor = Color(0xFF001945),
                    periodSelectorBorderColor = primary,
                    periodSelectorSelectedContainerColor = surfaceHigher,
                    periodSelectorUnselectedContainerColor = surfaceHighest,
                    periodSelectorSelectedContentColor = Color(0xFF001945),
                    periodSelectorUnselectedContentColor = Color(0xFF001945),
                )
            )
            if (validationError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = validationError,
                    color = primary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InputTimePickerPreview() {
    InputTimePicker(
        onConfirm = {},
        onDismiss = {},
        onCancel = {},
        onTimeChanged = {},
        initialTime = LocalTime.now(),
    )
}