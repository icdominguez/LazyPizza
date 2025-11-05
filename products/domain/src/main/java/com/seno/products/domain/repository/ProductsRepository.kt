package com.seno.products.domain.repository

import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.cart.CartItem
import com.seno.core.domain.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getAllProducts(): Flow<List<Product>>

    fun getExtraToppingsFlow(): Flow<List<Product>>

    fun getPizzaByName(pizzaName: String): Flow<Product.Pizza?>

    suspend fun createCart(items: List<CartItem>): FirebaseResult<String>

    suspend fun logoutAndDeleteCart(): FirebaseResult<Unit>
}
