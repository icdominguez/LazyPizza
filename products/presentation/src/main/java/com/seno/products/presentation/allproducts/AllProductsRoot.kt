package com.seno.products.presentation.allproducts

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
import com.seno.core.presentation.utils.ObserveAsEvents
import com.seno.products.presentation.allproducts.component.AllProductsTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllProductsRoot(
    onNavigateToAuthenticationScreen: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    viewModel: AllProductsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when (event) {
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

    Scaffold(
        topBar = {
            AllProductsTopBar(
                isLoggedIn = state.isLoggedIn,
                onLoginClick = {
                    onNavigateToAuthenticationScreen()
                },
                onLogoutClick = {
                    viewModel.onAction(AllProductsAction.ShowLogoutDialog)
                },
            )
        },
    ) { innerPadding ->
        AllProductsScreen(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
            state = state,
            onAction = { action ->
                when (action) {
                    is AllProductsAction.OnProductClicked -> onNavigateToProductDetail(action.pizzaName)
                    else -> viewModel.onAction(action)
                }
            },
        )
    }
}
