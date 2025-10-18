package com.seno.lazypizza.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object Menu {

        @Serializable
        data object AllProducts

        @Serializable
        data class ProductDetail(
            val pizzaName: String,
        )
    }

    @Serializable
    data object Cart {

        @Serializable
        data object CartScreen
    }

    @Serializable
    data object History {
        @Serializable
        data object HistoryScreen
    }
}