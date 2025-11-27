package com.seno.cart.presentation.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.cart.CartActions
import com.seno.cart.presentation.cart.CartState
import com.seno.cart.presentation.checkout.components.CommentsUI
import com.seno.cart.presentation.checkout.components.OrderCheckoutTopBar
import com.seno.cart.presentation.checkout.components.OrderDetailsUI
import com.seno.cart.presentation.checkout.components.PickUpTimeUI
import com.seno.cart.presentation.checkout.components.RecommendedAddOnsUI
import com.seno.core.presentation.theme.background
import com.seno.core.presentation.theme.surfaceHigher
import com.seno.core.presentation.utils.DeviceConfiguration

@Composable
fun OrderCheckoutScreen(
    cartState: CartState = CartState(),
    state: OrderCheckoutState = OrderCheckoutState(),
    onAction: (OrderCheckoutActions) -> Unit = {},
    onCartAction: (CartActions) -> Unit = {}
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val totalPrice =
        remember(cartState.cartItems) {
            cartState.cartItems.sumOf { it.price * it.quantity }
        }

    Scaffold(
        containerColor = background,
        topBar = {
            OrderCheckoutTopBar(
                modifier = Modifier
                    .background(
                        color = surfaceHigher,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    ),
                onBackClick = { onAction(OrderCheckoutActions.OnBackClick) },
            )
        },
        bottomBar = {
            OrderCheckoutBottomBar(
                isTablet = deviceType.isTablet(),
                totalPrice = totalPrice,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(surfaceHigher)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            PickUpTimeUI(
                state = state,
                onAction = onAction,
            )

            val cartItemsChunked = cartState.cartItems.chunked(if (deviceType.isTablet()) 2 else 1)

            OrderDetailsUI(
                cartItems = cartItemsChunked,
                onQuantityChange = { reference, quantity ->
                    onCartAction(
                        CartActions.OnCartItemQuantityChange(
                            reference = reference,
                            quantity = quantity,
                        ),
                    )
                },
                onDeleteClick = { onCartAction(CartActions.OnDeleteCartItemClick(it)) },
                isTablet = deviceType.isTablet(),
            )

            RecommendedAddOnsUI(
                recommendedItems = cartState.recommendedItems,
                onQuantityChange = { reference, quantity ->
                    onCartAction(
                        CartActions.OnCartItemQuantityChange(
                            reference = reference,
                            quantity = quantity,
                        ),
                    )
                },
            )

            CommentsUI(
                value = state.comment,
                onValueChange = { onAction(OrderCheckoutActions.OnCommentTextChange(it)) },
            )
        }
    }
}
