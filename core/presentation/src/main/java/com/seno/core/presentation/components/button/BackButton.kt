package com.seno.core.presentation.components.button

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.textSecondary8

@Composable
fun BackButton(onBackClick: () -> Unit) {
    Surface(
        modifier =
            Modifier
                .size(32.dp),
        shape = CircleShape,
        color = textSecondary8.copy(alpha = 0.08f),
        onClick = { onBackClick() },
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp),
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "back",
            tint = textSecondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BackButtonPreview() {
    BackButton { }
}
