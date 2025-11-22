package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.body_3_medium
import com.seno.core.presentation.theme.outline
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary

enum class RadioOptions(val text: String) {
    EARLIEST("Earliest available time"),
    SCHEDULE("Schedule time")
}

@Composable
internal fun RadioButtonSingleSelection(
    selectedOption: RadioOptions,
    onSelected: (RadioOptions) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.selectableGroup()) {
        RadioOptions.entries.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp)
                    .border(
                        width = 1.dp,
                        color = outline,
                        shape = CircleShape
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = primary,
                        unselectedColor = textSecondary
                    ),
                    selected = (option == selectedOption),
                    onClick = null
                )
                Text(
                    text = option.text,
                    style = body_3_medium.copy(
                        color = if (option == selectedOption) textPrimary else textSecondary
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}