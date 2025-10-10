package com.seno.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.background
import com.seno.core.presentation.theme.body_3_medium
import com.seno.core.presentation.theme.outline
import com.seno.core.presentation.theme.textPrimary

@Composable
fun ProductChip(
    modifier: Modifier = Modifier,
    chipText: String,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .background(color = background)
            .border(
                width = 1.dp,
                color = outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() } ,
                onClick = { onClick() }
            )
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = chipText,
            style = body_3_medium.copy(color = textPrimary)
        )
    }
}

@Preview
@Composable
fun FilterChipPreview() {
    ProductChip(
        chipText = "Pizza"
    )
}