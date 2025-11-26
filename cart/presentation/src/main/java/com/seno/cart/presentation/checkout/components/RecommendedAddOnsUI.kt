package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.cart.components.CartToppingCard
import com.seno.core.presentation.model.CartItemUI
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.outline
import com.seno.core.presentation.theme.textSecondary

@Composable
fun RecommendedAddOnsUI(
    recommendedItems: List<CartItemUI>,
    onQuantityChange: (String, Int) -> Unit,
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "RECOMMENDED ADD-ONS",
            style = label_2_semiBold,
            color = textSecondary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            itemsIndexed(recommendedItems) { index, recommendedItem ->
                CartToppingCard(
                    imageUrl = recommendedItem.image,
                    cartItem = recommendedItem,
                    onClick = {
                        onQuantityChange(
                            recommendedItem.reference,
                            recommendedItem.quantity,
                        )
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp, color = outline)
    }
}

@Preview
@Composable
private fun RecommendedAddOnsPreview() {
    RecommendedAddOnsUI(
        recommendedItems = listOf(),
        onQuantityChange = { _, _ -> },
    )
}
