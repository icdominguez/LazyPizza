package com.seno.products.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.utils.ObserveAsEvents
import com.seno.products.presentation.detail.component.ProductDetailTopBar
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

    Scaffold(
        topBar = {
            ProductDetailTopBar(
                onBackClick = { onBackClick() },
            )
        },
    ) { innerPadding ->
        ProductDetailScreen(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
            state = state,
            onAction = viewModel::onAction,
        )
    }
}
