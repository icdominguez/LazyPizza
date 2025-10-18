package com.seno.core.domain.product

sealed class Product {
    abstract val id: String
    abstract val type: ProductType
    abstract val name: String
    abstract val image: String
    abstract val price: Double

    data class Pizza(
        override val id: String,
        override val type: ProductType = ProductType.PIZZA,
        override val name: String,
        override val image: String,
        override val price: Double,
        val ingredients: List<String>
    ) : Product()

    data class Drink(
        override val id: String,
        override val type: ProductType = ProductType.DRINK,
        override val name: String,
        override val image: String,
        override val price: Double,
    ): Product()

    data class IceCream(
        override val id: String,
        override val type: ProductType = ProductType.ICE_CREAM,
        override val name: String,
        override val image: String,
        override val price: Double,
    ): Product()

    data class ExtraTopping(
        override val id: String,
        override val type: ProductType = ProductType.EXTRA_TOPPING,
        override val name: String,
        override val image: String,
        override val price: Double,
    ): Product()

    data class Sauce(
        override val id: String,
        override val type: ProductType = ProductType.SAUCE,
        override val name: String,
        override val image: String,
        override val price: Double,
    ): Product()
}