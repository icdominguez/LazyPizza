package com.seno.products.presentation.allproducts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllProductsRoot(
    onNavigateToProductDetail: (String) -> Unit,
    viewModel: AllProductsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AllProductsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is AllProductsAction.OnProductClicked -> onNavigateToProductDetail(action.pizzaName)
                else -> viewModel.onAction(action)
            }
        }
    )
}