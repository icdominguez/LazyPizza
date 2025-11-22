package com.seno.products.presentation.detail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailRoot(
    viewModel: ProductDetailViewModel = koinViewModel(),
    onAddToCartClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when (event) {
            is ProductDetailEvent.Error -> {
                Toast.makeText(context, event.error, Toast.LENGTH_LONG).show()
            }
            is ProductDetailEvent.OnCartSuccessfullySaved -> {
                onAddToCartClick()
            }
        }
    }

    ProductDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ProductDetailAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        },
    )
}
