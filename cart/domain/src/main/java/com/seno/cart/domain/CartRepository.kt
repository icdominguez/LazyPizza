package com.seno.cart.domain

import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.cart.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun createCart(items: List<CartItem>): FirebaseResult<String>

    suspend fun updateCart(
        cartId: String,
        items: List<CartItem>
    ): FirebaseResult<Unit>

    fun getCart(cartId: String): Flow<FirebaseResult<List<CartItem>>>
}
