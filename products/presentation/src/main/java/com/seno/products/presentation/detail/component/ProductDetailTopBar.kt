@file:OptIn(ExperimentalMaterial3Api::class)

package com.seno.products.presentation.detail.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seno.core.presentation.components.bar.LazyPizzaTopAppBar
import com.seno.core.presentation.components.button.BackButton

@Composable
fun ProductDetailTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyPizzaTopAppBar(
        navigationIcon = {
            BackButton { onBackClick() }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailTopBarPreview() {
    ProductDetailTopBar(
        onBackClick = {},
    )
}
