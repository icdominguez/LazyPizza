package com.seno.history.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_4_regular
import com.seno.core.presentation.theme.label_3_medium
import com.seno.core.presentation.theme.surfaceHigher
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_3
import com.seno.history.presentation.OrderItem

@Composable
fun HistoryItem(orderItem: OrderItem) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = surfaceHigher,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp,
                ),
        ) {
            Column {
                Text(
                    text = "Order #${orderItem.id}",
                    style = title_3,
                    color = textPrimary,
                )
                Text(
                    text = orderItem.date,
                    style = body_4_regular,
                    color = Color(0xFF627686),
                )

                Column(
                    modifier = Modifier.padding(
                        top = 16.dp,
                    ),
                ) {
                    orderItem.items.map {
                        Text(
                            text = it,
                            style = body_4_regular,
                            color = Color(0xFF101C28),
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = orderItem.status.color,
                            shape = RoundedCornerShape(16.dp),
                        ).padding(
                            vertical = 4.dp,
                            horizontal = 8.dp,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = orderItem.status.description,
                        style = label_3_medium,
                        color = textOnPrimary,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "Total amount",
                        style = body_4_regular,
                        color = textSecondary,
                    )

                    Text(
                        text = "$${orderItem.totalPrice}",
                        style = title_3,
                        color = textPrimary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HistoryItemComponent() {
    LazyPizzaTheme {
        HistoryItem(
            orderItem = OrderItem(
                id = "123456",
                date = "November 10, 12:24",
                items = listOf("1x Margherita"),
                totalPrice = 10.0,
            ),
        )
    }
}
