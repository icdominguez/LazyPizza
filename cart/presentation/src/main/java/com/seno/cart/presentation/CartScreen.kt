package com.seno.cart.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.components.EmptyCartComponent
import com.seno.core.presentation.components.LoadingComponent
import com.seno.core.presentation.components.card.ProductCard
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_1_medium
import com.seno.core.presentation.theme.textPrimary

@Composable
fun CartScreen(
    state: CartState = CartState(),
    onAction: (CartActions) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 21.dp),
            text = "Cart",
            style = body_1_medium.copy(
                color = textPrimary
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.isLoading) {
                LoadingComponent(text = "Getting your cart, please wait ...")
            } else if (state.cartItems.isEmpty()) {
                EmptyCartComponent(
                    modifier = Modifier
                        .padding(top = 120.dp),
                    onBackToMenuClick = { onAction(CartActions.OnNavigateToMenuClick) }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
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
                                    )
                                )
                            },
                            onDeleteClicked = { onAction(CartActions.OnDeleteCartItemClick(cartItem.reference)) },
                            extraToppings = cartItem.extraToppingsRelated,
                        )
                    }
                }
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