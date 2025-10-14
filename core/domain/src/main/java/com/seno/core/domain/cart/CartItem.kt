package com.seno.core.domain.cart

data class CartItem(
    val reference: String = "",
    val quantity: Int = 0,
    val extraToppings: List<ExtraTopping> = emptyList(),
)

data class ExtraTopping(
    val reference: String = "",
    val quantity: Int = 0,
)

fun CartItem.identityKey(): String {
    val signature =
        extraToppings
            .map { "${it.reference}:${it.quantity}" }
            .sorted()
            .joinToString("|")
    return if (signature.isEmpty()) reference else "$reference##$signature"
}
