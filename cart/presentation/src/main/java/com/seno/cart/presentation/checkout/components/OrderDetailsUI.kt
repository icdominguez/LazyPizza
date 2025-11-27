package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.components.card.ProductCard
import com.seno.core.presentation.model.CartItemUI
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.outline
import com.seno.core.presentation.theme.outline50
import com.seno.core.presentation.theme.textSecondary

@Composable
internal fun OrderDetailsUI(
    onQuantityChange: (String, Int) -> Unit,
    cartItems: List<List<CartItemUI>> = emptyList(),
    onDeleteClick: (String) -> Unit = {},
    isTablet: Boolean = false,
) {
    var showDetails by remember { mutableStateOf(false) }

    Column {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "ORDER DETAILS",
                style = label_2_semiBold,
                color = textSecondary,
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = outline50,
                    ).clickable(
                        enabled = cartItems.isNotEmpty(),
                        onClick = {
                            showDetails = !showDetails
                        },
                    ),
            ) {
                Icon(
                    imageVector = if (showDetails) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    tint = textSecondary,
                    contentDescription = "",
                )
            }
        }

        if (showDetails) {
            Spacer(modifier = Modifier.height(12.dp))

            cartItems.forEach { cartItems ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    cartItems.forEach { cartItem ->
                        ProductCard(
                            imageUrl = cartItem.image,
                            productName = cartItem.name,
                            productPrice = cartItem.price,
                            quantity = cartItem.quantity,
                            onQuantityChange = { newQuantity ->
                                onQuantityChange(
                                    cartItem.reference,
                                    newQuantity,
                                )
                            },
                            onDeleteClick = {
                                onDeleteClick(cartItem.reference)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    if (isTablet && cartItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(thickness = 1.dp, color = outline)
    }
}

@Preview
@Composable
private fun OrderDetailsUIPreview() {
    OrderDetailsUI(
        cartItems = listOf(),
        onQuantityChange = { _, _ -> },
    )
}
