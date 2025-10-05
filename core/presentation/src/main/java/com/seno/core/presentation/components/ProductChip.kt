package com.seno.core.presentation.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.seno.core.presentation.theme.body_3_medium
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.textPrimary

@Composable
fun ProductChip(
    chipText: String
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text(
                text = chipText,
                style = body_3_medium.copy(color = textPrimary)
            )
        },
        selected = selected,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = textOnPrimary
        ),
    )
}

@Preview
@Composable
fun FilterChipPreview() {
    ProductChip(
        chipText = "Pizza"
    )
}