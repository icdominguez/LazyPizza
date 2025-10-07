package com.seno.products.data.model

import com.seno.products.domain.model.PizzaItem

data class PizzaItemEntity(
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val ingredients: List<String> = emptyList()
)

fun PizzaItemEntity.toPizzaItem() = PizzaItem(
  name = name,
  image = image,
  price = price,
  ingredients = ingredients
)
