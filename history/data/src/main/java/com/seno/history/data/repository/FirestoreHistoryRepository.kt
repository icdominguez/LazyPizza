package com.seno.history.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.core.data.model.CheckoutOrderDto
import com.seno.core.data.model.ProductEntity
import com.seno.core.data.model.toProduct
import com.seno.core.domain.product.Product
import com.seno.core.domain.product.ProductType
import com.seno.core.domain.userdata.UserData
import com.seno.history.domain.HistoryRepository
import com.seno.history.domain.model.HistoryOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreHistoryRepository(
    private val db: FirebaseFirestore,
    private val userData: UserData,
) : HistoryRepository {
    override fun getHistoryOrders(): Flow<List<HistoryOrder>> =
        callbackFlow {
            val userId = userData.getUserId().first()
            if (userId == null) {
                trySend(emptyList())
                close()
                return@callbackFlow
            }

            val query = db
                .collection(ORDER_COLLECTIONS)
                .whereEqualTo("user_id", userId)

            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                launch {
                    val historyOrders = snapshot?.documents?.mapNotNull { document ->
                        val checkoutOrder = document.toObject(CheckoutOrderDto::class.java)
                        checkoutOrder?.let { co ->
                            val productsDeferred = co.items.map { cartItem ->
                                async(Dispatchers.IO) {
                                    val product = getProductsByReference(cartItem.reference)
                                    val quantity = cartItem.quantity

                                    "$quantity x ${product.name}"
                                }
                            }

                            val products = productsDeferred.awaitAll()

                            HistoryOrder(
                                id = document.id,
                                userId = co.userId,
                                orderNumber = co.orderNumber,
                                pickupTime = co.pickupTime,
                                items = products,
                                totalAmount = co.totalAmount,
                                status = co.status,
                                timestamp = co.timestamp,
                            )
                        }
                    } ?: emptyList()

                    trySend(historyOrders)
                }
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }

    private suspend fun getProductsByReference(references: String): Product {
        val products = try {
            val snapshot = db.document(references).get().await()
            val collectionName = snapshot.reference.parent.id

            snapshot
                .toObject(ProductEntity::class.java)
                ?.copy(
                    id = snapshot.id,
                    type = ProductType.valueOf(collectionName.dropLast(1).uppercase()),
                )?.toProduct()
        } catch (e: Exception) {
            throw Exception("Failed to retrieve product by reference '$references'. Details: ${e.message}")
        }

        return products
            ?: throw IllegalStateException("Failed to parse product data for reference '$references'.")
    }

    companion object {
        const val ORDER_COLLECTIONS = "orders"
    }
}
