package com.seno.cart.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.cart.domain.CartRepository
import com.seno.core.data.model.ProductEntity
import com.seno.core.data.model.toProduct
import com.seno.core.domain.product.Product
import com.seno.core.domain.product.ProductType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreCartRepository(
    private val db: FirebaseFirestore
) : CartRepository {
    override fun getRecommendedProducts(): Flow<List<Product>> {
        val collections =
            ProductType.entries
                .filter {
                    it == ProductType.DRINK || it == ProductType.SAUCE
                }.map { type ->
                    type.name.lowercase() + "s" to type
                }

        val flows = collections.map { (collection, type) ->
            callbackFlow {
                val listener =
                    db
                        .collection(collection)
                        .addSnapshotListener { snapshot, error ->
                            if (error != null) {
                                trySend(emptyList())
                                return@addSnapshotListener
                            }

                            val entities =
                                snapshot?.documents.orEmpty().mapNotNull { document ->
                                    document
                                        .toObject(ProductEntity::class.java)
                                        ?.copy(id = document.id)
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

    override fun getProductsByReference(references: List<String>): Flow<List<Product>> =
        flow {
            val products =
                references.mapNotNull { documentReferences ->
                    try {
                        val snapshot = db.document(documentReferences).get().await()
                        val collectionName = snapshot.reference.parent.id

                        snapshot
                            .toObject(ProductEntity::class.java)
                            ?.copy(
                                id = snapshot.id,
                                type = ProductType.valueOf(collectionName.dropLast(1).uppercase()),
                            )?.toProduct()
                    } catch (e: Exception) {
                        null
                    }
                }
            emit(products)
        }
}
