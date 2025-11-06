package com.seno.core.domain.repository

import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.cart.CartItem
import kotlinx.coroutines.flow.Flow

interface CoreRepository {
    suspend fun updateCart(
        cartId: String,
        items: List<CartItem>
    ): FirebaseResult<Unit>

    fun getCart(cartId: String): Flow<FirebaseResult<List<CartItem>>>
}
