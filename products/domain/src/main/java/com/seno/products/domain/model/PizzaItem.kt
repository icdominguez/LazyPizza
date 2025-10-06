package com.seno.products.domain.model

data class PizzaItem(
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val ingredients: List<String> = emptyList()
)
