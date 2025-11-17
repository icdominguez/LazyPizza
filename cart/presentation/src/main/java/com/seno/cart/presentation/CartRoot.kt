package com.seno.cart.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.cart.presentation.components.CartTopBar
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
        when (event) {
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

    Scaffold(
        topBar = {
            CartTopBar()
        },
    ) { innerPadding ->
        CartScreen(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
            state = state,
            onAction = { action ->
                when (action) {
                    is CartActions.OnNavigateToMenuClick -> onNavigateToMenu()
                    else -> viewModel.onAction(action)
                }
            },
        )
    }
}
