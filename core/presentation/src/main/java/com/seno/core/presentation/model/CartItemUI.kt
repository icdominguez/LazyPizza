package com.seno.core.presentation.model

import com.seno.core.domain.cart.CartItem
import com.seno.core.domain.product.ProductType

data class CartItemUI(
    val reference: String,
    val quantity: Int = 0,
    val image: String,
    val name: String,
    val price: Double,
    val type: ProductType,
    val ingredients: List<String> = emptyList(),
    val extraToppingsRelated: List<String> = emptyList(),
)

fun CartItemUI.toCartItem() =
    CartItem(
        reference = reference,
        quantity = quantity,
    )
