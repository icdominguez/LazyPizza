package com.seno.products.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.components.LazyPizzaDefaultScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailRoot(
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyPizzaDefaultScreen {
        ProductDetailScreen(
            state = state,
            onAction = {}
        )
    }
}