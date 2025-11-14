package com.seno.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.history.presentation.component.HistoryInformationComponent
import com.seno.history.presentation.component.HistoryItem

@Composable
internal fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (state.isLoggedIn) {
        true ->
            if (state.orderItems.isEmpty()) {
                HistoryInformationComponent(
                    title = stringResource(R.string.no_orders_yet),
                    description = stringResource(R.string.your_our_will_appear_here),
                    buttonText = stringResource(R.string.go_to_menu),
                    onClick = {
                        onAction(HistoryAction.OnGoToMenuClick)
                    },
                )
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(if (deviceType.isTablet()) 2 else 1),
                    contentPadding = PaddingValues(16.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.orderItems) { orderItem ->
                        HistoryItem(orderItem = orderItem)
                    }
                }
            }

        false -> HistoryInformationComponent(
            title = stringResource(R.string.not_signed_in),
            description = stringResource(R.string.please_sign_in_to_view_your_order_history),
            buttonText = stringResource(R.string.sign_in),
            onClick = {
                onAction(HistoryAction.OnSingInClick)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenNotLoggedInPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(isLoggedIn = true),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenLoggedInWithNoOrdersPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(
                isLoggedIn = true,
                orderItems = emptyList(),
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenLoggedInWithOrdersPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(
                isLoggedIn = true,
                orderItems = listOf(
                    OrderItem(
                        id = "12347",
                        date = "November 10, 12:22",
                        items = listOf("1 x Margherita"),
                        totalPrice = 8.99,
                    ),
                    OrderItem(
                        id = "12345",
                        date = "November 1, 13:45",
                        items = listOf(
                            "1 x Margherita",
                            "2 x Pepsi",
                            "2 x Cookies Ice Cream",
                        ),
                        totalPrice = 25.45,
                        status = OrderStatus.COMPLETED,
                    ),
                    OrderItem(
                        id = "12346",
                        date = "October 28, 19:53",
                        items = listOf(
                            "1 x Margherita",
                            "2 x Cookies Ice Cream",
                        ),
                        totalPrice = 11.78,
                        status = OrderStatus.COMPLETED,
                    ),
                ),
            ),
            onAction = {},
        )
    }
}
