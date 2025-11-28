package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.R
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.label_2_medium
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_medium
import com.seno.core.presentation.theme.title_3

@Composable
internal fun OrderSuccess(
    orderNumber: String,
    pickupTime: String,
    onBackToMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Min)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = stringResource(R.string.place_order_success_title),
            style = title_1_medium,
        )
        Text(
            text = stringResource(R.string.place_order_success_subtitle),
            style = body_3_regular,
            color = textSecondary,
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp),
                ).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.place_order_number),
                    style = label_2_medium,
                    color = textSecondary,
                )
                Text(
                    text = orderNumber,
                    style = label_2_semiBold,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.place_order_pickup),
                    style = label_2_medium,
                    color = textSecondary,
                )
                Text(
                    text = pickupTime,
                    style = label_2_semiBold,
                )
            }
        }

        TextButton(
            onClick = onBackToMenuClick,
        ) {
            Text(
                text = stringResource(R.string.back_to_menu),
                style = title_3,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderSuccessPreview() {
    LazyPizzaTheme {
        OrderSuccess(
            orderNumber = "#12345",
            pickupTime = "September 25, 12:15",
            onBackToMenuClick = {},
        )
    }
}
