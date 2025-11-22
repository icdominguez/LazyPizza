package com.seno.cart.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderCheckoutRoot(
    viewModel: OrderCheckoutViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OrderCheckoutScreen(
        state = state,
        onAction = viewModel::onAction
    )
}