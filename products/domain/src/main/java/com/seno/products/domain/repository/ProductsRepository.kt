package com.seno.products.domain.repository

import com.seno.products.domain.model.MenuItem
import com.seno.products.domain.model.PizzaItem
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun drinks(): Flow<List<MenuItem>>
    fun sauces(): Flow<List<MenuItem>>
    fun iceCreams(): Flow<List<MenuItem>>
    fun extraToppings(): Flow<List<MenuItem>>
    fun pizzas(): Flow<List<PizzaItem>>
}