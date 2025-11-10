package com.seno.core.presentation.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.seno.core.presentation.circleGradientShadow
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.title_3

@Composable
fun LazyPizzaPrimaryButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        enabled = enabled,
        onClick = {
            onClick()
        },
        modifier = modifier.circleGradientShadow(enabled),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        } else {
            Text(
                text = buttonText,
                style = title_3,
                color = textOnPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LazyPizzaPrimaryButtonPreview() {
    LazyPizzaTheme {
        Column {
            LazyPizzaPrimaryButton(
                isLoading = false,
                buttonText = "Button",
                onClick = {},
            )
            LazyPizzaPrimaryButton(
                isLoading = false,
                buttonText = "Button",
                onClick = {},
                enabled = false,
            )
        }
    }
}
