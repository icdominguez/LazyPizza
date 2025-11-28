package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.outline
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.surfaceHigher
import com.seno.core.presentation.theme.surfaceHighest
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_3
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTimePicker(
    onConfirm: (LocalTime) -> Unit,
    onCancel: () -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    initialTime: LocalTime? = null,
    validationError: String? = null
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = initialTime?.hour ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = initialTime?.minute ?: currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    LaunchedEffect(timePickerState.hour, timePickerState.minute, onTimeChange) {
        val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
        onTimeChange(selectedTime)
    }

    BasicAlertDialog(
        onDismissRequest = onCancel,
    ) {
        Surface(
            modifier = Modifier
                .width(264.dp),
            shape = RoundedCornerShape(28.dp),
            color = surfaceHigher,
        ) {
            Column {
                // Title
                Text(
                    text = "Select Time".uppercase(),
                    modifier = Modifier.padding(16.dp),
                    style = label_2_semiBold.copy(color = textSecondary),
                )

                // Time Input
                TimeInput(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 16.dp),
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        containerColor = surfaceHighest,
                        timeSelectorSelectedContainerColor = surfaceHigher,
                        timeSelectorUnselectedContainerColor = surfaceHighest,
                    ),
                )

                // Validation Error
                if (validationError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = validationError,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = primary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                        maxLines = 2,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.Start),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = outline)
                }

                // Action Buttons
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp, bottom = 24.dp, top = 8.dp),
                ) {
                    TextButton(
                        onClick = onCancel,
                    ) {
                        Text(
                            text = "Cancel",
                            style = title_3.copy(color = primary),
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    LazyPizzaPrimaryButton(
                        onClick = {
                            if (validationError == null) {
                                val selectedTime = LocalTime.of(
                                    timePickerState.hour,
                                    timePickerState.minute,
                                )
                                onConfirm(selectedTime)
                            }
                        },
                        buttonText = "Ok",
                        enabled = validationError == null,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InputTimePickerPreview() {
    LazyPizzaTheme {
        InputTimePicker(
            onConfirm = {},
            onCancel = {},
            onTimeChange = {},
            initialTime = LocalTime.now(),
        )
    }
}
