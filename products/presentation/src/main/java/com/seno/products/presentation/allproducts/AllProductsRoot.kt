package com.seno.products.presentation.allproducts

import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllProductsRoot(
    onNavigateToProductDetail: (String) -> Unit,
    viewModel: AllProductsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(
        flow = viewModel.event
    ) { event ->
        when(event) {
            is AllProductsEvent.Error -> {
                Toast.makeText(context, event.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    if (state.isCreateNewCart) {
        Dialog(
            onDismissRequest = {},
        ) {
            CircularProgressIndicator()
        }
    }

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