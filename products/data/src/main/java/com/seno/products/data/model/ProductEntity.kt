package com.seno.products.data.model

import com.seno.products.domain.model.Product
import com.seno.products.domain.model.Product.*
import com.seno.products.domain.model.ProductType

data class ProductEntity(
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val type: ProductType = ProductType.PIZZA,
    val ingredients: List<String>? = null,
)

fun ProductEntity.toProduct(): Product? {
    return when (type) {
        ProductType.PIZZA -> Pizza(
            type = type,
            name = name,
            price = price,
            image = image,
            ingredients = ingredients.orEmpty()
        )

        ProductType.DRINK -> Drink(type = type, name = name, price = price, image = image)
        ProductType.ICE_CREAM -> IceCream(type = type, name = name, price = price, image = image)
        ProductType.SAUCE -> IceCream(type = type, name = name, price = price, image = image)
        ProductType.EXTRA_TOPPING -> IceCream(type = type, name = name, price = price, image = image)
    }
}