package com.seno.products.presentation.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProductDetailScreen(
    state: ProductDetailState = ProductDetailState(),
    onAction: (ProductDetailAction) -> Unit = {},
) {
    Text("Product detail")
}

@Preview
@Composable
private fun ProductDetailScreenPreview() {
    ProductDetailScreen()
}