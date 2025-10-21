package com.seno.cart.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.cart.domain.CartRepository
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.cart.CartItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreCartRepository(
    private val db: FirebaseFirestore
) : CartRepository {
    override suspend fun createCart(items: List<CartItem>): FirebaseResult<String> = try {
        val cartId = db.collection(CART_COLLECTION).document().id
        val cartReference = db.collection(CART_COLLECTION).document(cartId)

        // 1. Create the cart document empty
        cartReference.set(mapOf<String, Any>()).await()

        val batch = db.batch()

        // 2. Iterate through items and create them
        items.forEach { item ->
            val itemReference = cartReference.collection(CART_ITEMS_COLLECTION).document()
            batch.set(itemReference, item)
        }

        // 3. Save everything
        batch.commit().await()

        FirebaseResult.Success(cartId)
    } catch (exception: Exception) {
        FirebaseResult.Error(exception)
    }

    override suspend fun updateCart(cartId: String, items: List<CartItem>): FirebaseResult<Unit> =
        try {
            val cartReference = db.collection(CART_COLLECTION).document(cartId)
            val itemsReference = cartReference.collection(CART_ITEMS_COLLECTION)

            val snapshot = itemsReference.get().await()

            val existingItems = snapshot.documents.mapNotNull { document ->
                val item = document.toObject(CartItem::class.java)
                item?.reference?.let { reference -> reference to document.id }
            }.toMap()

            val batch = db.batch()
            val newReferences = items.map { it.reference }.toSet()

            existingItems.forEach { (reference, documentId) ->
                if (reference !in newReferences) {
                    batch.delete(itemsReference.document(documentId))
                }
            }

            items.forEach { item ->
                val documentId = existingItems[item.reference]
                if(documentId != null) {
                    val documentReference = itemsReference.document(documentId)
                    if(item.quantity == 0) {
                        batch.delete(documentReference)
                    } else {
                        batch.update(documentReference, CartItem::quantity.name, item.quantity)
                    }
                } else if (item.quantity > 0) {
                    val documentReference = itemsReference.document()
                    batch.set(documentReference, item)
                }
            }

            batch.commit().await()
            FirebaseResult.Success(Unit)
        } catch (exception: Exception) {
            FirebaseResult.Error(exception)
        }

    override fun getCart(cartId: String): Flow<FirebaseResult<List<CartItem>>> = callbackFlow {
        val cartReference = db.collection(CART_COLLECTION).document(cartId)
        val listener = cartReference
            .collection(CART_ITEMS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(FirebaseResult.Error(error))
                    return@addSnapshotListener
                }

                if(snapshot == null) {
                    trySend(FirebaseResult.Success(emptyList()))
                    return@addSnapshotListener
                }

                try {
                    val cartItems = snapshot.documents.mapNotNull { document ->
                        document.toObject(CartItem::class.java)
                    }

                    trySend(FirebaseResult.Success(cartItems))
                } catch (exception: Exception) {
                    trySend(FirebaseResult.Error(exception))
                }
        }

        awaitClose { listener.remove() }
    }
    companion object {
        private const val CART_COLLECTION = "cart"
        private const val CART_ITEMS_COLLECTION = "items"
    }
}