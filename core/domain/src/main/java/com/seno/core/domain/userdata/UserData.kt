package com.seno.core.domain.userdata

import kotlinx.coroutines.flow.Flow

interface UserData {
    fun getCartId(): Flow<String?>
    suspend fun setCardId(cartId: String)
    suspend fun clear()
}