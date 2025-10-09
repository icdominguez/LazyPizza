package com.seno.products.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.products.data.model.ProductEntity
import com.seno.products.data.model.toProduct
import com.seno.products.domain.model.Product
import com.seno.products.domain.model.ProductType
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

class FirestoreProductRepository(
    private val db: FirebaseFirestore
) : ProductsRepository {
    override fun getAllProducts(): Flow<List<Product>> = getAllProductsFlow()

    private fun getAllProductsFlow(): Flow<List<Product>> {
        val collections = listOf(
            PIZZAS to ProductType.PIZZA,
            DRINKS to ProductType.DRINK,
            SAUCES to ProductType.SAUCE,
            ICE_CREAMS to ProductType.ICE_CREAM,
        )

        val flows = collections.map { (collection, type) ->
            callbackFlow {
                val listener = db.collection(collection)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(emptyList())
                            return@addSnapshotListener
                        }

                        val entities = snapshot?.toObjects(ProductEntity::class.java).orEmpty()
                        val products = entities.mapNotNull { it.copy(type = type).toProduct() }
                        trySend(products)
                    }

                awaitClose { listener.remove() }
            }
        }

        return combine(flows) { lists ->
            lists.flatMap { it }
        }
    }

    override fun getExtraToppingsFlow(): Flow<List<Product>> =
        callbackFlow {
            val listener = db.collection(EXTRA_TOPPINGS)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    val entities = snapshot?.toObjects(ProductEntity::class.java).orEmpty()
                    val products = entities.mapNotNull {
                        it.copy(type = ProductType.EXTRA_TOPPING).toProduct()
                    }
                    trySend(products)
                }

            awaitClose { listener.remove() }
        }

    override fun getPizzaByName(pizzaName: String): Flow<Product.Pizza?> =
        callbackFlow {
            val listener = db.collection(PIZZAS)
                .whereEqualTo("name", pizzaName)
                .limit(1)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(null)
                        return@addSnapshotListener
                    }

                    val pizza = snapshot?.documents
                        ?.firstOrNull()
                        ?.toObject(ProductEntity::class.java)
                        ?.toProduct() as? Product.Pizza

                    trySend(pizza)
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