package com.seno.cart.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.components.CartToppingCard
import com.seno.cart.presentation.components.EmptyCartComponent
import com.seno.core.presentation.components.LoadingComponent
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.components.card.ProductCard
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.surfaceHigher
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.core.presentation.utils.applyIf
import com.seno.core.presentation.utils.currentDeviceConfiguration
import com.seno.core.presentation.utils.formatToPrice

@Composable
fun CartScreen(
    state: CartState = CartState(),
    onAction: (CartActions) -> Unit = {},
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            MobileCartScreenUI(state, onAction)
        }

        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            TabletCartScreenUI(state, onAction)
        }
    }
}

@Composable
private fun TabletCartScreenUI(
    state: CartState,
    onAction: (CartActions) -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.isLoading) {
            LoadingComponent(text = "Getting your cart, please wait ...")
        } else if (state.cartItems.isEmpty()) {
            EmptyCartComponent(
                modifier =
                    Modifier
                        .padding(top = 120.dp),
                onBackToMenuClick = { onAction(CartActions.OnNavigateToMenuClick) },
            )
        } else {
            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // Left side — Cart items
                LazyColumn(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.cartItems) { cartItem ->
                        ProductCard(
                            imageUrl = cartItem.image,
                            productName = cartItem.name,
                            productPrice = cartItem.price,
                            quantity = cartItem.quantity,
                            onQuantityChange = { quantity ->
                                onAction(
                                    CartActions.OnCartItemQuantityChange(
                                        reference = cartItem.reference,
                                        quantity = quantity,
                                    ),
                                )
                            },
                            onDeleteClick = {
                                onAction(CartActions.OnDeleteCartItemClick(cartItem.reference))
                            },
                            extraToppings = cartItem.extraToppingsRelated,
                        )
                    }
                }

                // Right side — Recommendations
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .background(
                                color = surfaceHigher,
                                shape = RoundedCornerShape(16.dp),
                            ).padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    if (state.recommendedItems.isNotEmpty()) {
                        Column {
                            Text(
                                text = stringResource(R.string.recommended_title).uppercase(),
                                style = label_2_semiBold.copy(color = textSecondary),
                                modifier = Modifier.padding(bottom = 8.dp),
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                            ) {
                                items(state.recommendedItems) { recommendedItem ->
                                    CartToppingCard(
                                        imageUrl = recommendedItem.image,
                                        cartItem = recommendedItem,
                                        onClick = {
                                            onAction(
                                                CartActions.OnCartItemQuantityChange(
                                                    reference = recommendedItem.reference,
                                                    quantity = recommendedItem.quantity,
                                                ),
                                            )
                                        },
                                    )
                                }
                            }
                        }
                        val totalPrice =
                            remember(state.cartItems) {
                                state.cartItems.sumOf { it.price * it.quantity }
                            }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyPizzaPrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            buttonText = "Proceed to Checkout (${totalPrice.formatToPrice()})",
                            onClick = { /* Checkout click */ },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MobileCartScreenUI(
    state: CartState,
    onAction: (CartActions) -> Unit
) {
    val totalPrice =
        remember(state.cartItems) {
            state.cartItems.sumOf { it.price * it.quantity }
        }

    val deviceType = currentDeviceConfiguration()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.isLoading) {
                LoadingComponent(text = "Getting your cart, please wait ...")
            } else if (state.cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .applyIf(
                            deviceType != DeviceConfiguration.MOBILE_LANDSCAPE,
                        ) {
                            padding(top = 120.dp)
                        },
                    contentAlignment = if (deviceType == DeviceConfiguration.MOBILE_LANDSCAPE) {
                        Alignment.Center
                    } else {
                        Alignment.TopCenter
                    },
                ) {
                    EmptyCartComponent(
                        onBackToMenuClick = { onAction(CartActions.OnNavigateToMenuClick) },
                    )
                }
            } else {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 140.dp),
                ) {
                    items(state.cartItems) { cartItem ->
                        ProductCard(
                            imageUrl = cartItem.image,
                            productName = cartItem.name,
                            productPrice = cartItem.price,
                            quantity = cartItem.quantity,
                            onQuantityChange = { quantity ->
                                onAction(
                                    CartActions.OnCartItemQuantityChange(
                                        reference = cartItem.reference,
                                        quantity = quantity,
                                    ),
                                )
                            },
                            onDeleteClick = { onAction(CartActions.OnDeleteCartItemClick(cartItem.reference)) },
                            extraToppings = cartItem.extraToppingsRelated,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        if (state.recommendedItems.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.recommended_title).uppercase(),
                                style =
                                    label_2_semiBold.copy(
                                        color = textSecondary,
                                    ),
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                            )

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                            ) {
                                itemsIndexed(state.recommendedItems) { index, recommendedItem ->
                                    CartToppingCard(
                                        imageUrl = recommendedItem.image,
                                        cartItem = recommendedItem,
                                        onClick = {
                                            onAction(
                                                CartActions.OnCartItemQuantityChange(
                                                    reference = recommendedItem.reference,
                                                    quantity = recommendedItem.quantity,
                                                ),
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!state.isLoading && state.cartItems.isNotEmpty()) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(125.dp)
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                                            MaterialTheme.colorScheme.surface,
                                        ),
                                ),
                        ),
                contentAlignment = Alignment.BottomCenter,
            ) {
                LazyPizzaPrimaryButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp),
                    buttonText = "Proceed to Checkout ($${totalPrice.formatToPrice()})",
                    onClick = { /* Checkout click */ },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenPreview() {
    LazyPizzaTheme {
        CartScreen(CartState(isLoading = true))
    }
}
