package com.seno.products.data.model

import com.seno.products.domain.model.MenuItem

data class MenuItemEntity(
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0
)

fun MenuItemEntity.toMenuItem() = MenuItem(
    name = name,
    image = image,
    price = price
)


