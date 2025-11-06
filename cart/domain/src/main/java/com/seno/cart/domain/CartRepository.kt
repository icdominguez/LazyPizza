package com.seno.cart.domain

import com.seno.core.domain.product.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getRecommendedProducts(): Flow<List<Product>>

    fun getProductsByReference(references: List<String>): Flow<List<Product>>
}
