package com.seno.cart.presentation

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
fun CartRoot(
    viewModel: CartViewModel = koinViewModel(),
    onNavigateToMenu: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when(event) {
            is CartEvents.Error -> {
                Toast.makeText(context, event.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    if (state.isUpdatingCart) {
        Dialog(
            onDismissRequest = {},
        ) {
            CircularProgressIndicator()
        }
    }

    CartScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is CartActions.OnNavigateToMenuClick -> onNavigateToMenu()
                else -> viewModel.onAction(action)
            }
        }
    )
}