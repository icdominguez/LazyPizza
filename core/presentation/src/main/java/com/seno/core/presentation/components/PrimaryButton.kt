package com.seno.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.primaryGradient
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.title_3

@Composable
fun LazyPizzaPrimaryButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = primaryGradient,
                        startX = 0f,
                        endX = Float.POSITIVE_INFINITY
                    ),
                    shape = CircleShape
                )
                .dropShadow(
                    RoundedCornerShape(20.dp)
                ) {
                    radius = 10f
                    color = primary
                    spread = 6f
                    alpha = 0.25f
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                style = title_3,
                color = textOnPrimary
            )
        }
    }
}

@Preview
@Composable
fun LazyPizzaPrimaryButtonPreview() {
    LazyPizzaPrimaryButton(
        buttonText = "Button",
        onClick = {},
    )
}