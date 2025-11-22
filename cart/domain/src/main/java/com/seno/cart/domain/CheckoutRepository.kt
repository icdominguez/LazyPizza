package com.seno.cart.domain

import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.checkout.CheckoutOrder

interface CheckoutRepository {
    suspend fun sendOrder(checkoutOrder: CheckoutOrder): FirebaseResult<Unit>
}
