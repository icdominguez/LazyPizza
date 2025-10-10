package com.seno.products.domain.repository

import com.seno.products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getExtraToppingsFlow(): Flow<List<Product>>
    fun getPizzaByName(pizzaName: String): Flow<Product.Pizza?>
}