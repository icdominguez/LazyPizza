package com.seno.products.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.core.data.model.ProductEntity
import com.seno.core.data.model.toCharDto
import com.seno.core.data.model.toProduct
import com.seno.core.data.repository.FirestoreCoreRepository.Companion.CART_COLLECTION
import com.seno.core.data.repository.FirestoreCoreRepository.Companion.CART_ITEMS_COLLECTION
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.cart.CartItem
import com.seno.core.domain.product.Product
import com.seno.core.domain.product.ProductType
import com.seno.core.domain.userdata.UserData
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class FirestoreProductRepository(
    private val userData: UserData,
    private val db: FirebaseFirestore
) : ProductsRepository {
    override fun getAllProducts(): Flow<List<Product>> = getAllProductsFlow()

    private fun getAllProductsFlow(): Flow<List<Product>> {
        val collections =
            ProductType.entries
                .filterNot { it == ProductType.EXTRA_TOPPING }
                .map { type ->
                    type.name.lowercase() + "s" to type
                }

        val flows =
            collections.map { (collection, type) ->
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

                                val products =
                                    entities.mapNotNull { it.copy(type = type).toProduct() }
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
            val listener =
                db
                    .collection(ProductType.EXTRA_TOPPING.name.lowercase() + "s")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(emptyList())
                            return@addSnapshotListener
                        }

                        val entities =
                            snapshot?.documents.orEmpty().mapNotNull { document ->
                                document.toObject(ProductEntity::class.java)?.copy(id = document.id)
                            }
                        val products =
                            entities.mapNotNull {
                                it.copy(type = ProductType.EXTRA_TOPPING).toProduct()
                            }
                        trySend(products)
                    }

            awaitClose { listener.remove() }
        }

    override fun getPizzaByName(pizzaName: String): Flow<Product.Pizza?> =
        callbackFlow {
            val listener =
                db
                    .collection(ProductType.PIZZA.name.lowercase() + "s")
                    .whereEqualTo("name", pizzaName)
                    .limit(1)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(null)
                            return@addSnapshotListener
                        }

                        val pizza =
                            snapshot
                                ?.documents
                                ?.firstOrNull()
                                ?.toObject(ProductEntity::class.java)
                                ?.copy(id = snapshot.documents.first().id)
                                ?.toProduct() as? Product.Pizza

                        trySend(pizza)
                    }

            awaitClose { listener.remove() }
        }

    override suspend fun createCart(items: List<CartItem>): FirebaseResult<String> =
        try {
            val cartId = db.collection(CART_COLLECTION).document().id
            val cartReference = db.collection(CART_COLLECTION).document(cartId)

            // 1. Create the cart document empty
            cartReference.set(mapOf<String, Any>()).await()

            val batch = db.batch()

            // 2. Iterate through items and create them
            items.map { it.toCharDto() }.forEach { item ->
                val itemReference = cartReference.collection(CART_ITEMS_COLLECTION).document()
                batch.set(itemReference, item)
            }

            // 3. Save everything
            batch.commit().await()
            val totalItemCount = items.sumOf { it.quantity }
            userData.setTotalItemCart(totalItemCount)
            FirebaseResult.Success(cartId)
        } catch (exception: Exception) {
            FirebaseResult.Error(exception)
        }

    /**
     * Logs out the user by deleting their entire shopping cart from Firestore and clearing local user data.
     *
     * This function performs the following steps in a single Firestore transaction:
     * 1. Retrieves the current user's cart ID from local storage.
     * 2. Fetches all items within the corresponding cart in Firestore.
     * 3. Deletes all cart item documents.
     * 4. Deletes the parent cart document.
     * 5. Clears all user data from the local `UserData` store.
     *
     * If any step fails, the entire transaction is rolled back, and an error is returned.
     *
     * @return [FirebaseResult.Success] with [Unit] if the cart is successfully deleted and user data is cleared.
     * @return [FirebaseResult.Error] if the cart ID is not found or if any Firestore operation fails.
     */
    override suspend fun logoutAndDeleteCart(): FirebaseResult<Unit> {
        return try {
            val cartId = userData.getCartId().first()
                ?: return FirebaseResult.Error(Exception("Cart id is null"))
            val cartReference = db.collection(CART_COLLECTION).document(cartId)
            val cartItems = cartReference.collection(CART_ITEMS_COLLECTION).get().await()

            db
                .runTransaction { transaction ->
                    for (document in cartItems.documents) {
                        transaction.delete(document.reference)
                    }

                    transaction.delete(cartReference)
                }.await()
            userData.clear()
            FirebaseResult.Success(Unit)
        } catch (exception: Exception) {
            FirebaseResult.Error(exception)
        }
    }
}
