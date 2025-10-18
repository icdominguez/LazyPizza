package com.seno.products.presentation.allproducts

import com.seno.core.domain.product.Product
import com.seno.core.domain.product.ProductType

data class AllProductsState(
    val isLoading: Boolean = true,
    val products: Map<ProductType, List<Product>> = emptyMap(),
    val headerIndexMap: Map<ProductType, Int> = emptyMap(),
    val productsFiltered: Map<ProductType, List<Product>> = emptyMap(),
    val searchQuery: String = "",
    val productQuantities: Map<String, Int> = emptyMap()
)