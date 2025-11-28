package com.seno.cart.presentation.cart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_medium

@Composable
fun EmptyCartComponent(
    modifier: Modifier = Modifier,
    onBackToMenuClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Your cart is empty",
            style =
                title_1_medium.copy(
                    color = textPrimary,
                ),
        )

        Spacer(modifier = Modifier.size(6.dp))

        Text(
            text = "Head back to the menu and grab a pizza you love",
            style =
                body_3_regular.copy(
                    color = textSecondary,
                ),
        )

        Spacer(modifier = Modifier.size(20.dp))

        LazyPizzaPrimaryButton(
            buttonText = "Back to menu",
            onClick = { onBackToMenuClick() },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyCartComponentPreview() {
    EmptyCartComponent()
}
