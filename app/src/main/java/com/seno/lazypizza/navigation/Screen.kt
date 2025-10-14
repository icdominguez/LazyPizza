package com.seno.lazypizza.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object AllProducts

    @Serializable
    data class ProductDetail(
        val pizzaName: String,
    )
}