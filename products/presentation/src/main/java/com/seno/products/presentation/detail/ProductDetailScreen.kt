@file:OptIn(ExperimentalMaterial3Api::class)

package com.seno.products.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.products.presentation.detail.component.ProductDetailMobile
import com.seno.products.presentation.detail.component.ProductDetailTablet
import com.seno.products.presentation.detail.component.ProductDetailTopBar

@Composable
internal fun ProductDetailScreen(
    state: ProductDetailState = ProductDetailState(),
    onAction: (ProductDetailAction) -> Unit = {},
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        ProductDetailTopBar(
            onBackClick = { onAction(ProductDetailAction.OnBackClick) },
        )

        if (deviceType.isTablet()) {
            ProductDetailTablet(
                state = state,
                onAction = onAction,
            )
        } else {
            ProductDetailMobile(
                state = state,
                onAction = onAction,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailScreenPreview() {
    LazyPizzaTheme {
        ProductDetailScreen()
    }
}
