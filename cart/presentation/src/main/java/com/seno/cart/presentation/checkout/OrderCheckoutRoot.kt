package com.seno.cart.presentation.checkout

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.cart.presentation.cart.CartViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderCheckoutRoot(
    cartViewModel: CartViewModel = koinViewModel(),
    viewModel: OrderCheckoutViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val cartState by cartViewModel.state.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (cartState.isUpdatingCart) {
        Dialog(
            onDismissRequest = {},
        ) {
            CircularProgressIndicator()
        }
    }

    OrderCheckoutScreen(
        cartState = cartState,
        state = state,
        onAction = { action ->
            when (action) {
                OrderCheckoutActions.OnBackClick -> navigateBack()
                else -> viewModel.onAction(action)
            }
        },
        onCartAction = {
            cartViewModel.onAction(it)
        },
    )
}
