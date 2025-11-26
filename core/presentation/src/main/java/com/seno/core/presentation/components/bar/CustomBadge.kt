package com.seno.core.presentation.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.title_4

@Composable
fun CustomBadge(
    modifier: Modifier = Modifier,
    items: Int = 0
) {
    Box(
        modifier = modifier
            .size(16.dp)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                spotColor = primary,
            ).background(
                color = primary,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$items",
            style = title_4,
            color = textOnPrimary,
        )
    }
}

@Preview
@Composable
private fun CustomBadgePreview() {
    CustomBadge(
        items = 9,
    )
}
