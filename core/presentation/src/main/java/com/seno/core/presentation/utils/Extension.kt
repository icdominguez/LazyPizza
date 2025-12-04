package com.seno.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Modifier.applyIf(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier =
    if (condition) {
        then(modifier())
    } else {
        this
    }

fun String.replaceUnderscores() = this.replace("_", " ")

fun String.toCamelCase() =
    this
        .lowercase()
        .split(' ', '_')
        .filter { it.isNotEmpty() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }

fun Double.formatToPrice(): String = String.format(Locale.ROOT, "%.2f", this)

fun Long.toPlaceOrderPickupTime(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("MMMM dd, HH:mm", Locale.getDefault())
    return formatter.format(date)
}
