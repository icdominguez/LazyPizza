package com.seno.cart.presentation.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.R
import com.seno.cart.presentation.cart.CartActions
import com.seno.cart.presentation.cart.CartState
import com.seno.cart.presentation.checkout.components.CommentsUI
import com.seno.cart.presentation.checkout.components.OrderCheckoutTopBar
import com.seno.cart.presentation.checkout.components.OrderDetailsUI
import com.seno.cart.presentation.checkout.components.OrderSuccess
import com.seno.cart.presentation.checkout.components.PickUpTimeUI
import com.seno.cart.presentation.checkout.components.RecommendedAddOnsUI
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.background
import com.seno.core.presentation.theme.surfaceHigher
import com.seno.core.presentation.utils.DeviceConfiguration

@Composable
fun OrderCheckoutScreen(
    onAction: (OrderCheckoutActions) -> Unit,
    onCartAction: (CartActions) -> Unit,
    cartState: CartState = CartState(),
    state: OrderCheckoutState = OrderCheckoutState(),
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val totalPrice =
        remember(cartState.cartItems) {
            cartState.cartItems.sumOf { it.price * it.quantity }
        }

    LaunchedEffect(cartState.cartItems, state.placeOrderSuccess, onAction) {
        if (cartState.cartItems.isEmpty() && !state.placeOrderSuccess) {
            onAction(OrderCheckoutActions.OnBackClick)
        }
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
            if (state.placeOrderNumber.isEmpty()) {
                if (state.isUserLoggedIn) {
                    OrderCheckoutBottomBar(
                        isPlaceOrderLoading = state.isPlaceOrderLoading,
                        isPlaceOrderSuccess = state.placeOrderSuccess,
                        isTablet = deviceType.isTablet(),
                        totalPrice = totalPrice,
                        onPlaceOrderClick = {
                            onAction(OrderCheckoutActions.OnPlaceOrderClick(totalPrice))
                        },
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LazyPizzaPrimaryButton(
                            buttonText = stringResource(R.string.login_to_place_order),
                            onClick = {
                                onAction(OrderCheckoutActions.OnLoginClick)
                            },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        if (state.placeOrderSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceHigher),
                contentAlignment = Alignment.TopCenter,
            ) {
                OrderSuccess(
                    modifier =
                        Modifier
                            .padding(top = 120.dp),
                    orderNumber = state.placeOrderNumber,
                    pickupTime = state.placeOrderPickupTime,
                    onBackToMenuClick = { onAction(OrderCheckoutActions.OnMainMenuClick) },
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceHigher)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            ) {
                PickUpTimeUI(
                    state = state,
                    onAction = onAction,
                )

                val cartItemsChunked =
                    cartState.cartItems.chunked(if (deviceType.isTablet()) 2 else 1)

                AnimatedVisibility(
                    cartItemsChunked.isNotEmpty(),
                ) {
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
                }

                AnimatedVisibility(cartState.recommendedItems.isNotEmpty()) {
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
                }

                CommentsUI(
                    value = state.comment,
                    onValueChange = { onAction(OrderCheckoutActions.OnCommentTextChange(it)) },
                )
            }
        }
    }
}
