package com.seno.cart.presentation.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.checkout.components.PickUpTimeUI

@Composable
fun OrderCheckoutScreen(
    state: OrderCheckoutState,
    onAction: (OrderCheckoutActions) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        PickUpTimeUI(
            state = state,
            onAction = onAction
        )
    }
}