package com.seno.core.domain.userdata

import kotlinx.coroutines.flow.Flow

interface UserData {
    fun getCartId(): Flow<String?>

    suspend fun setCardId(cartId: String)

    fun getTotalItemCart(): Flow<Int>

    suspend fun setTotalItemCart(totalItem: Int)

    fun getUserId(): Flow<String?>

    suspend fun setUserId(phoneNumber: String)

    suspend fun clear()
}
