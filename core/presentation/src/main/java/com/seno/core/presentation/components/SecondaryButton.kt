package com.seno.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.primary8
import com.seno.core.presentation.theme.title_3

@Composable
fun LazyPizzaSecondaryButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        border = BorderStroke(
            width = 1.dp,
            color = primary8
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Text(
            text = buttonText,
            style = title_3,
            color = primary
        )
    }
}

@Preview
@Composable
fun LazyPizzaSecondaryButtonPreview() {
    LazyPizzaSecondaryButton(
        buttonText = "Button",
        onClick = {},
    )
}