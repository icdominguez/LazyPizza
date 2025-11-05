package com.seno.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.cart.domain.CartRepository
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.cart.identityKey
import com.seno.core.domain.product.ProductType
import com.seno.core.domain.repository.CoreRepository
import com.seno.core.domain.userdata.UserData
import com.seno.core.presentation.model.CartItemUI
import com.seno.core.presentation.model.toCartItem
import com.seno.core.presentation.utils.SnackbarAction
import com.seno.core.presentation.utils.SnackbarController
import com.seno.core.presentation.utils.SnackbarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val coreRepository: CoreRepository,
    private val userData: UserData
) : ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _event = Channel<CartEvents>()
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            val allDrinksAndSnacks = loadAllDrinksAndSnacks().shuffled()
            _state.update {
                it.copy(
                    recommendedItems =
                        allDrinksAndSnacks.take(
                            MAX_RECOMMENDED_ITEMS_TO_SHOW,
                        ),
                )
            }

            val cartId = userData.getCartId().first()
            cartId?.let {
                coreRepository.getCart(it).collect { response ->
                    when (response) {
                        is FirebaseResult.Success -> {
                            val cartItems = response.data
                            if (cartItems.isEmpty()) {
                                _state.update { state ->
                                    state.copy(
                                        cartItems = emptyList(),
                                        cartId = it,
                                        isLoading = false,
                                        recommendedItems =
                                            allDrinksAndSnacks.take(
                                                MAX_RECOMMENDED_ITEMS_TO_SHOW,
                                            ),
                                    )
                                }
                                return@collect
                            }

                            val products =
                                cartRepository
                                    .getProductsByReference(
                                        cartItems.map { item -> item.reference },
                                    ).first()

                            val cartItemsUI =
                                products.mapIndexed { index, product ->
                                    val cartItem = cartItems[index]
                                    if (product.type == ProductType.PIZZA) {
                                        val extraToppings =
                                            cartRepository
                                                .getProductsByReference(
                                                    cartItem.extraToppings.map { it.reference },
                                                ).first()

                                        val formattedExtras =
                                            extraToppings.map { topping ->
                                                val qty =
                                                    cartItem.extraToppings
                                                        .find {
                                                            it.reference.substringAfterLast("/") == topping.id
                                                        }?.quantity ?: 0
                                                "${qty}x ${topping.name}"
                                            }

                                        val extraCost =
                                            extraToppings.sumOf { topping ->
                                                val qty =
                                                    cartItem.extraToppings
                                                        .find {
                                                            it.reference.substringAfterLast("/") == topping.id
                                                        }?.quantity ?: 0
                                                topping.price * qty
                                            }

                                        CartItemUI(
                                            reference = cartItem.identityKey(),
                                            quantity = cartItem.quantity,
                                            image = product.image,
                                            name = product.name,
                                            price = product.price + extraCost,
                                            type = product.type,
                                            extraToppingsRelated = formattedExtras,
                                        )
                                    } else {
                                        CartItemUI(
                                            reference = cartItem.reference,
                                            quantity = cartItem.quantity,
                                            image = product.image,
                                            name = product.name,
                                            price = product.price,
                                            type = product.type,
                                        )
                                    }
                                }

                            _state.update { state ->
                                state.copy(
                                    cartId = it,
                                    cartItems = cartItemsUI,
                                    isLoading = false,
                                    recommendedItems =
                                        getFilteredRecommendedItems(
                                            allDrinksAndSnacks,
                                            cartItemsUI,
                                        ),
                                )
                            }
                        }

                        is FirebaseResult.Error -> {
                            _event.trySend(
                                CartEvents.Error(
                                    response.exception.message ?: "Error getting the cart",
                                ),
                            )
                        }
                    }
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadAllDrinksAndSnacks(): List<CartItemUI> =
        try {
            val recommendedProduct = cartRepository.getRecommendedProducts().first()

            recommendedProduct
                .map { product ->
                    CartItemUI(
                        reference = "${product.type.name.lowercase()}s/${product.id}",
                        quantity = 1,
                        image = product.image,
                        name = product.name,
                        price = product.price,
                        type = product.type,
                    )
                }
        } catch (e: Exception) {
            _event.trySend(
                CartEvents.Error(
                    error = "Failed to load all drinks and sauces: ${e.message}",
                ),
            )

            emptyList()
        }

    private fun getFilteredRecommendedItems(
        allDrinksAndSnacks: List<CartItemUI>,
        cartItems: List<CartItemUI>
    ): List<CartItemUI> {
        val currentRefs = cartItems.map { it.reference }
        return allDrinksAndSnacks.filter { it.reference !in currentRefs }.take(10)
    }

    fun onAction(action: CartActions) {
        when (action) {
            is CartActions.OnCartItemQuantityChange ->
                onCartItemQuantityChange(
                    action.reference,
                    action.quantity,
                )

            is CartActions.OnDeleteCartItemClick -> onDeleteProductClick(action.reference)
            else -> Unit
        }
    }

    private fun onCartItemQuantityChange(
        reference: String,
        quantity: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isUpdatingCart = true) }

            try {
                val current = _state.value
                val existing = current.cartItems.find { it.reference == reference }
                val recommended = current.recommendedItems.find { it.reference == reference }

                when {
                    // Add new recommended item
                    existing == null && recommended != null && quantity > 0 -> {
                        val newItem = recommended.copy(quantity = quantity)
                        val newCart = (current.cartItems + newItem).map { it.toCartItem() }

                        when (val result = coreRepository.updateCart(current.cartId, newCart)) {
                            is FirebaseResult.Success -> {
                                _state.update {
                                    it.copy(
                                        cartItems = current.cartItems + newItem,
                                        recommendedItems = current.recommendedItems.filterNot { it.reference == reference },
                                        isUpdatingCart = false,
                                    )
                                }
                                SnackbarController.sendEvent(
                                    SnackbarEvent(
                                        "${recommended.name} added to cart",
                                        SnackbarAction("OK") {},
                                    ),
                                )
                            }

                            is FirebaseResult.Error -> {
                                _state.update { it.copy(isUpdatingCart = false) }
                                _event.trySend(CartEvents.Error("Failed to add item: ${result.exception.message}"))
                            }
                        }
                    }

                    // Remove item
                    existing != null && quantity == 0 -> {
                        val updated =
                            current.cartItems
                                .filterNot { it.reference == reference }
                                .map { it.toCartItem() }
                        coreRepository.updateCart(current.cartId, updated)
                        _state.update { it.copy(isUpdatingCart = false) }

                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                "${existing.name} removed from cart",
                                SnackbarAction("OK") {},
                            ),
                        )
                    }

                    // Update quantity
                    existing != null && quantity > 0 -> {
                        val updatedCartItems =
                            current.cartItems.map {
                                if (it.reference == reference) it.copy(quantity = quantity) else it
                            }
                        val updated = updatedCartItems.map { it.toCartItem() }
                        coreRepository.updateCart(current.cartId, updated)
                        _state.update {
                            it.copy(
                                cartItems = updatedCartItems,
                                isUpdatingCart = false,
                            )
                        }
                    }

                    else -> _state.update { it.copy(isUpdatingCart = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isUpdatingCart = false) }
                _event.trySend(CartEvents.Error("Failed to update cart: ${e.message}"))
            }
        }
    }

    private fun onDeleteProductClick(reference: String) {
        _state.update { it.copy(isUpdatingCart = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val current = _state.value
            val itemName = current.cartItems.find { it.reference == reference }?.name

            val updated =
                current.cartItems.filterNot { it.reference == reference }.map { it.toCartItem() }
            coreRepository.updateCart(current.cartId, updated)

            _state.update { it.copy(isUpdatingCart = false) }

            SnackbarController.sendEvent(
                SnackbarEvent("$itemName removed from cart", SnackbarAction("OK") {}),
            )
        }
    }

    companion object {
        const val MAX_RECOMMENDED_ITEMS_TO_SHOW = 10
    }
}
