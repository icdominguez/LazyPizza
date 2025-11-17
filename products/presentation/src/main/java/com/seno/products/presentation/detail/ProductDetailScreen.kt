@file:OptIn(ExperimentalMaterial3Api::class)

package com.seno.products.presentation.detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.products.presentation.detail.component.ProductDetailMobile
import com.seno.products.presentation.detail.component.ProductDetailTablet

@Composable
internal fun ProductDetailScreen(
    state: ProductDetailState = ProductDetailState(),
    onAction: (ProductDetailAction) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    if (deviceType.isTablet()) {
        ProductDetailTablet(
            modifier = modifier,
            state = state,
            onAction = onAction,
        )
    } else {
        ProductDetailMobile(
            modifier = modifier,
            state = state,
            onAction = onAction,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailScreenPreview() {
    LazyPizzaTheme {
        ProductDetailScreen()
    }
}
