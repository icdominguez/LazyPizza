package com.seno.cart.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.cart.domain.CheckoutRepository
import com.seno.cart.domain.orderNumber
import com.seno.core.data.model.toDto
import com.seno.core.data.repository.FirestoreCoreRepository.Companion.CART_COLLECTION
import com.seno.core.data.repository.FirestoreCoreRepository.Companion.CART_ITEMS_COLLECTION
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.checkout.CheckoutOrder
import com.seno.core.domain.userdata.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class FirestoreCheckoutRepository(
    private val db: FirebaseFirestore,
    private val userData: UserData,
) : CheckoutRepository {
    override suspend fun sendOrder(checkoutOrder: CheckoutOrder): FirebaseResult<orderNumber> {
        return try {
            val cartId = userData.getCartId().first()
                ?: return FirebaseResult.Error(Exception("Cart id is null"))

            val cartReference = db.collection(CART_COLLECTION).document(cartId)
            val cartItems = cartReference.collection(CART_ITEMS_COLLECTION).get().await()

            db
                .runTransaction { transaction ->
                    val newOrderRef = db.collection(ORDER_COLLECTIONS).document()
                    transaction.set(newOrderRef, checkoutOrder.toDto())

                    for (document in cartItems.documents) {
                        transaction.delete(document.reference)
                    }

                    transaction.delete(cartReference)
                }.await()
            userData.setTotalItemCart(0)

            FirebaseResult.Success(checkoutOrder.orderNumber)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    companion object {
        const val ORDER_COLLECTIONS = "orders"
    }
}
