package com.seno.cart.presentation.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.label_1_medium
import com.seno.core.presentation.theme.label_1_semiBold
import com.seno.core.presentation.theme.surfaceHigher
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.utils.formatToPrice

@Composable
fun OrderCheckoutBottomBar(
    isTablet: Boolean = false,
    totalPrice: Double = 0.0
) {
    if (isTablet) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "ORDER TOTAL: ",
                    style = label_1_medium,
                    color = textSecondary,
                )

                Text(
                    text = "$${totalPrice.formatToPrice()}",
                    style = label_1_semiBold,
                    color = textPrimary,
                )

                Spacer(modifier = Modifier.weight(1f))

                LazyPizzaPrimaryButton(
                    modifier = Modifier
                        .width(380.dp),
                    buttonText = "Place order",
                    onClick = { /* */ },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        Column(
            modifier = Modifier
                .background(surfaceHigher)
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "ORDER TOTAL:",
                    style = label_1_medium,
                    color = textSecondary,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$${totalPrice.formatToPrice()}",
                    style = label_1_semiBold,
                    color = textPrimary,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyPizzaPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Place order",
                onClick = { /* */ },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderCheckoutMobileBottomBarPreview() {
    OrderCheckoutBottomBar()
}

@Preview(showBackground = true)
@Composable
private fun OrderCheckoutTabletBottomBarPreview() {
    OrderCheckoutBottomBar(isTablet = true)
}
