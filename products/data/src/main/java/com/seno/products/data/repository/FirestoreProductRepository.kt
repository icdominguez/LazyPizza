package com.seno.products.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.products.data.model.MenuItemEntity
import com.seno.products.data.model.PizzaItemEntity
import com.seno.products.data.model.toMenuItem
import com.seno.products.data.model.toPizzaItem
import com.seno.products.domain.model.MenuItem
import com.seno.products.domain.model.PizzaItem
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreProductRepository(
    private val db: FirebaseFirestore
): ProductsRepository {
    override fun drinks(): Flow<List<MenuItem>> = sectionFlow(DRINKS)
    override fun sauces(): Flow<List<MenuItem>> = sectionFlow(SAUCES)
    override fun iceCreams(): Flow<List<MenuItem>> = sectionFlow(ICE_CREAMS)
    override fun extraToppings(): Flow<List<MenuItem>> = sectionFlow(EXTRA_TOPPINGS)
    override fun pizzas(): Flow<List<PizzaItem>> = pizzasFlow()

    private fun sectionFlow(collection: String): Flow<List<MenuItem>> = callbackFlow {
        val listener = db.collection(collection)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val items = snap?.toObjects(MenuItemEntity::class.java).orEmpty()
                trySend(items.map { it.toMenuItem() })
            }

        awaitClose { listener.remove() }
    }

    private fun pizzasFlow(): Flow<List<PizzaItem>> = callbackFlow {
        val listener = db.collection(PIZZAS)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val items = snap?.toObjects(PizzaItemEntity::class.java).orEmpty()
                trySend(items.map { it.toPizzaItem() })
            }
        awaitClose { listener.remove() }
    }

    private companion object {
        const val DRINKS = "drinks"
        const val SAUCES = "sauces"
        const val ICE_CREAMS = "ice creams"
        const val EXTRA_TOPPINGS = "extra_toppings"
        const val PIZZAS = "pizzas"
    }
}