package com.seno.core.data.model

import com.seno.core.domain.product.Product
import com.seno.core.domain.product.Product.Drink
import com.seno.core.domain.product.Product.ExtraTopping
import com.seno.core.domain.product.Product.IceCream
import com.seno.core.domain.product.Product.Pizza
import com.seno.core.domain.product.Product.Sauce
import com.seno.core.domain.product.ProductType

data class ProductEntity(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val type: ProductType = ProductType.PIZZA,
    val ingredients: List<String>? = null,
)

fun ProductEntity.toProduct(): Product? =
    when (type) {
        ProductType.PIZZA -> Pizza(id = id, type = type, name = name, price = price, image = image, ingredients = ingredients.orEmpty())
        ProductType.DRINK -> Drink(id = id, type = type, name = name, price = price, image = image)
        ProductType.ICE_CREAM -> IceCream(id = id, type = type, name = name, price = price, image = image)
        ProductType.SAUCE -> Sauce(id = id, type = type, name = name, price = price, image = image)
        ProductType.EXTRA_TOPPING ->
            ExtraTopping(
                id = id,
                type = type,
                name = name,
                price = price,
                image = image,
            )
    }
