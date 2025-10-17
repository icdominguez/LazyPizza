package com.seno.products.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.products.data.model.ProductEntity
import com.seno.products.data.model.toProduct
import com.seno.core.domain.product.Product
import com.seno.core.domain.product.ProductType
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
        val collections = ProductType.entries
            .filterNot { it == ProductType.EXTRA_TOPPING }
            .map { type ->
                type.name.lowercase() + "s" to type
            }

        val flows = collections.map { (collection, type) ->
            callbackFlow {
                val listener = db.collection(collection)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(emptyList())
                            return@addSnapshotListener
                        }

                        val entities = snapshot?.documents.orEmpty().mapNotNull { document ->
                            document.toObject(ProductEntity::class.java)?.copy(id = document.id)
                        }

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
            val listener = db.collection(ProductType.EXTRA_TOPPING.name.lowercase() + "s")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    val entities = snapshot?.documents.orEmpty().mapNotNull { document ->
                        document.toObject(ProductEntity::class.java)?.copy(id = document.id)
                    }
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
                        ?.copy(id = snapshot.documents.first().id)
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