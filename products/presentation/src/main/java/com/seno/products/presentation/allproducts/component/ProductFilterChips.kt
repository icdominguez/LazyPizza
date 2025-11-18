package com.seno.products.presentation.allproducts.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.domain.product.ProductType
import com.seno.core.presentation.components.chip.ProductChip
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.utils.toCamelCase

@Composable
fun ProductFilterChips(
    onChipClick: (ProductType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier =
            modifier
                .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(
            ProductType.entries,
            key = { it.name },
        ) { productType ->
            if (productType != ProductType.EXTRA_TOPPING) {
                ProductChip(
                    chipText = productType.name.toCamelCase(),
                    onClick = {
                        onChipClick(productType)
                    },
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductFilterChipsPreview() {
    LazyPizzaTheme {
        ProductFilterChips(
            onChipClick = {
            },
        )
    }
}
