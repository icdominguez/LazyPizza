package com.seno.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.core.data.model.CartDto
import com.seno.core.data.model.identityKey
import com.seno.core.data.model.toCartItem
import com.seno.core.data.model.toCharDto
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.cart.CartItem
import com.seno.core.domain.repository.CoreRepository
import com.seno.core.domain.userdata.UserData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreCoreRepository(
    private val db: FirebaseFirestore,
    private val userData: UserData
) : CoreRepository {
    override suspend fun updateCart(
        cartId: String,
        items: List<CartItem>
    ): FirebaseResult<Unit> =
        try {
            val cartReference = db.collection(CART_COLLECTION).document(cartId)
            val itemsReference = cartReference.collection(CART_ITEMS_COLLECTION)

            val snapshot = itemsReference.get().await()

            val existingItems =
                snapshot.documents
                    .mapNotNull { document ->
                        val item = document.toObject(CartDto::class.java)
                        item?.let { it.identityKey() to document.id }
                    }.toMap()

            val batch = db.batch()
            val newKeys = items.map { it.toCharDto().identityKey() }.toSet()

            existingItems.forEach { (key, documentId) ->
                if (key !in newKeys) {
                    batch.delete(itemsReference.document(documentId))
                }
            }

            items.map { it.toCharDto() }.forEach { item ->
                val key = item.identityKey()
                val documentId = existingItems[key]

                if (documentId != null) {
                    val documentReference = itemsReference.document(documentId)
                    if (item.quantity == 0) {
                        batch.delete(documentReference)
                    } else {
                        batch.update(documentReference, CartDto::quantity.name, item.quantity)
                    }
                } else if (item.quantity > 0) {
                    val documentReference = itemsReference.document()
                    batch.set(documentReference, item)
                }
            }

            batch.commit().await()
            val totalItemCount = items.sumOf { it.quantity }
            userData.setTotalItemCart(totalItemCount)
            FirebaseResult.Success(Unit)
        } catch (exception: Exception) {
            FirebaseResult.Error(exception)
        }

    override fun getCart(cartId: String): Flow<FirebaseResult<List<CartItem>>> =
        callbackFlow {
            val cartReference = db.collection(CART_COLLECTION).document(cartId)
            val listener =
                cartReference
                    .collection(CART_ITEMS_COLLECTION)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(FirebaseResult.Error(error))
                            return@addSnapshotListener
                        }

                        if (snapshot == null) {
                            trySend(FirebaseResult.Success(emptyList()))
                            return@addSnapshotListener
                        }

                        try {
                            val cartItems =
                                snapshot.documents
                                    .mapNotNull { document ->
                                        document.toObject(CartDto::class.java)
                                    }.map { it.toCartItem() }

                            trySend(FirebaseResult.Success(cartItems))
                        } catch (exception: Exception) {
                            trySend(FirebaseResult.Error(exception))
                        }
                    }

            awaitClose { listener.remove() }
        }

    companion object {
        const val CART_COLLECTION = "cart"
        const val CART_ITEMS_COLLECTION = "items"
    }
}
