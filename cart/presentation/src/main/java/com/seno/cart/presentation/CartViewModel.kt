package com.seno.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.cart.domain.CartRepository
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.product.ProductType
import com.seno.core.domain.userdata.UserData
import com.seno.core.presentation.model.CartItemUI
import com.seno.core.presentation.model.toCartItem
import com.seno.products.domain.repository.ProductsRepository
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
    private val productRepository: ProductsRepository,
    private val userData: UserData
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _event = Channel<CartEvents>()
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {

            _state.update { it.copy(isLoading = true) }

            val cartId = userData.getCartId().first()

            cartId?.let {
                // 1. Get all cart items. Now we need to keep the flow open to listen for changes in all products screen
                cartRepository.getCart(cartId).collect { cartItemsResponse ->
                    when(cartItemsResponse) {
                        is FirebaseResult.Success -> {
                            val cartItems = cartItemsResponse.data
                            // 2. Get products in cart to know the price, images etc
                            val productsInCart = productRepository.getProductsByReference(
                                references = cartItems.map { it.reference }
                            ).first()

                            // 3. Iterate through products to create cart items
                            val cartItemsUI = productsInCart.mapIndexed { index, product ->
                                // If the product is a pizza it could have extra toppings. So we need to get them and calculate the price. Also create the corresponding string for showing them
                                if(product.type == ProductType.PIZZA) {
                                    val pizzaExtraToppings = productRepository.getProductsByReference(
                                        references = cartItems[index].extraToppings.map { it.reference }
                                    ).first()

                                    val pizzaExtraToppingsFormatted = pizzaExtraToppings.map { extraTopping ->
                                        val quantity = cartItems[index].extraToppings.find { it.reference.substringAfterLast("/") == extraTopping.id }?.quantity ?: 0
                                        "${quantity}x ${extraTopping.name}"
                                    }

                                    val extraToppingsTotal = pizzaExtraToppings.sumOf { extraTopping ->
                                        val quantity = cartItems[index].extraToppings.find { it.reference.substringAfterLast("/") == extraTopping.id }?.quantity ?: 0
                                        extraTopping.price * quantity
                                    }

                                    val totalPrice = product.price + extraToppingsTotal

                                    CartItemUI(
                                        reference = cartItems[index].reference,
                                        quantity = cartItems[index].quantity,
                                        image = product.image,
                                        name = product.name,
                                        price = totalPrice,
                                        type = product.type,
                                        extraToppingsRelated = pizzaExtraToppingsFormatted,
                                    )
                                } else {
                                    CartItemUI(
                                        reference = cartItems[index].reference,
                                        quantity = cartItems[index].quantity,
                                        image = product.image,
                                        name = product.name,
                                        price = product.price,
                                        type = product.type,
                                    )
                                }
                            }

                            // 4. Update cart with the data
                            _state.update { state ->
                                state.copy(
                                    cartItems = cartItemsUI,
                                    cartId = cartId,
                                    isLoading = false,
                                )
                            }

                        }
                        is FirebaseResult.Error -> {
                            _event.trySend(CartEvents.Error(cartItemsResponse.exception.message ?: "Error getting the cart"))
                        }
                    }
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onAction(action: CartActions) {
        when (action) {
            is CartActions.OnCartItemQuantityChange -> onCartItemQuantityChange(action.reference, action.quantity)
            is CartActions.OnDeleteCartItemClick -> onDeleteProductClick(action.reference)
            else -> Unit
        }
    }

    private fun onCartItemQuantityChange(reference: String, quantity: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val updatedCartItems = _state.value.cartItems.toMutableList().apply {
                val selectedCartItemIndex = this.indexOfFirst { it.reference == reference }
                if(selectedCartItemIndex != -1) {
                    this[selectedCartItemIndex] = this[selectedCartItemIndex].copy(quantity = quantity)
                }
            }.map { it.toCartItem() }

            cartRepository.updateCart(_state.value.cartId, updatedCartItems)
        }
    }

    private fun onDeleteProductClick(reference: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val updatedCartItems = _state.value.cartItems.filter { it.reference != reference }.map { it.toCartItem() }
            cartRepository.updateCart(_state.value.cartId, updatedCartItems)
        }
    }
}