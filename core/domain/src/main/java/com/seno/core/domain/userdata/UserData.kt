package com.seno.core.domain.userdata

import kotlinx.coroutines.flow.Flow

interface UserData {
    fun getCartId(): Flow<String?>

    suspend fun setCardId(cartId: String)

    fun getTotalItemCart(): Flow<Int>

    suspend fun setTotalItemCart(totalItem: Int)

    fun getIsLogin(): Flow<Boolean>

    suspend fun setIsLogin(isLogin: Boolean)

    suspend fun clear()
}
