@file:OptIn(ExperimentalCoroutinesApi::class)

package com.seno.products.presentation.allproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.cart.domain.CartRepository
import com.seno.core.domain.FirebaseResult
import com.seno.core.domain.product.Product
import com.seno.core.domain.product.ProductType
import com.seno.core.domain.userdata.UserData
import com.seno.core.presentation.model.CartItemUI
import com.seno.core.presentation.model.toCartItem
import com.seno.core.presentation.utils.SnackbarAction
import com.seno.core.presentation.utils.SnackbarController
import com.seno.core.presentation.utils.SnackbarEvent
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllProductsViewModel(
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val userData: UserData,
) : ViewModel() {
    private val _state = MutableStateFlow(AllProductsState())
    val state = _state.asStateFlow()

    private val _event = Channel<AllProductsEvent>()
    val event = _event.receiveAsFlow()

    private var cartId: String? = null

    init {
        userData
            .getCartId()
            .distinctUntilChanged()
            .onEach {
                cartId = it
            }.launchIn(viewModelScope)

        loadProducts()
    }

    fun onAction(action: AllProductsAction) {
        when (action) {
            is AllProductsAction.OnQueryChange -> {
                val productsFiltered =
                    if (action.newSearchQuery.isNotEmpty()) {
                        _state.value.products
                            .mapValues { (_, products) ->
                                products.filter { product ->
                                    product.name.contains(action.newSearchQuery, ignoreCase = true)
                                }
                            }.filterValues { it.isNotEmpty() }
                    } else {
                        state.value.products
                    }

                _state.update {
                    it.copy(
                        searchQuery = action.newSearchQuery,
                        productsFiltered = productsFiltered,
                    )
                }
            }

            is AllProductsAction.OnProductMinus -> onProductMinus(action.productState)
            is AllProductsAction.OnProductPlus -> onProductPlus(action.productState)
            is AllProductsAction.OnProductDelete -> onProductDelete(action.productState)
            else -> Unit
        }
    }

    private fun onProductPlus(product: CartItemUI) {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItemSelected = product.copy(quantity = product.quantity + 1).toCartItem()
            if (cartId == null) {
                _state.update {
                    it.copy(isCreateNewCart = true)
                }
                val createCartResponse =
                    cartRepository.createCart(items = listOf(cartItemSelected))

                when (createCartResponse) {
                    is FirebaseResult.Success -> {
                        userData.setCardId(createCartResponse.data)
                        SnackbarController.sendEvent(
                            event =
                                SnackbarEvent(
                                    message = "${product.name} added to cart",
                                    action =
                                        SnackbarAction(
                                            name = "OK",
                                            action = {},
                                        ),
                                ),
                        )
                    }

                    is FirebaseResult.Error -> {
                        _event.trySend(
                            AllProductsEvent.Error(
                                createCartResponse.exception.message
                                    ?: "Error creating the cart",
                            ),
                        )
                    }
                }
            } else {
                cartId?.let {
                    val getCartResponse = cartRepository.getCart(it).first()

                    when (getCartResponse) {
                        is FirebaseResult.Success -> {
                            val updatedCartItems =
                                getCartResponse.data.toMutableList().apply {
                                    val cartItemSelectedIndex = this.indexOf(cartItemSelected)
                                    if (cartItemSelectedIndex == -1) {
                                        this.add(cartItemSelected)
                                    } else {
                                        this[cartItemSelectedIndex] =
                                            this[cartItemSelectedIndex].copy(quantity = product.quantity + 1)
                                    }
                                }
                            cartRepository.updateCart(
                                cartId = it,
                                items = updatedCartItems,
                            )

                            if (!getCartResponse.data.contains(product.toCartItem())) {
                                SnackbarController.sendEvent(
                                    event =
                                        SnackbarEvent(
                                            message = "${product.name} added to cart",
                                            action =
                                                SnackbarAction(
                                                    name = "OK",
                                                    action = {},
                                                ),
                                        ),
                                )
                            }
                        }

                        is FirebaseResult.Error -> {
                            _event.trySend(
                                AllProductsEvent.Error(
                                    getCartResponse.exception.message ?: "Error updating the cart",
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onProductMinus(product: CartItemUI) {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItemSelected = product.copy(quantity = product.quantity - 1).toCartItem()
            if (cartId == null) {
                cartRepository.createCart(
                    items =
                        listOf(
                            product.toCartItem().copy(quantity = 1),
                        ),
                )
            } else {
                cartId?.let {
                    val getCartResponse = cartRepository.getCart(it).first()

                    when (getCartResponse) {
                        is FirebaseResult.Success -> {
                            val updatedCartItems =
                                getCartResponse.data.toMutableList().apply {
                                    val cartItemSelectedIndex = this.indexOf(cartItemSelected)
                                    if (cartItemSelectedIndex == -1) {
                                        this.add(cartItemSelected)
                                    } else {
                                        this[cartItemSelectedIndex] =
                                            this[cartItemSelectedIndex].copy(quantity = product.quantity - 1)
                                    }
                                }
                            cartRepository.updateCart(
                                cartId = it,
                                items = updatedCartItems,
                            )

                            val productQuantity =
                                getCartResponse.data.find { cartItem ->
                                    cartItem.reference == product.toCartItem().reference
                                }

                            if (productQuantity?.quantity == 1) {
                                SnackbarController.sendEvent(
                                    event =
                                        SnackbarEvent(
                                            message = "${product.name} removed from cart",
                                            action =
                                                SnackbarAction(
                                                    name = "OK",
                                                    action = {},
                                                ),
                                        ),
                                )
                            }
                        }

                        is FirebaseResult.Error -> {
                            _event.trySend(
                                AllProductsEvent.Error(
                                    getCartResponse.exception.message ?: "Error updating the cart",
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onProductDelete(product: CartItemUI) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val cartItemToDelete = product.toCartItem()
            cartId?.let { cartId ->
                val getCartResponse = cartRepository.getCart(cartId).first()

                when (getCartResponse) {
                    is FirebaseResult.Success -> {
                        val updatedCartItems =
                            getCartResponse
                                .data
                                .filter { it.reference != cartItemToDelete.reference }

                        cartRepository.updateCart(
                            cartId = cartId,
                            items = updatedCartItems,
                        )

                        SnackbarController.sendEvent(
                            event =
                                SnackbarEvent(
                                    message = "${product.name} removed from cart",
                                    action =
                                        SnackbarAction(
                                            name = "OK",
                                            action = {},
                                        ),
                                ),
                        )
                    }

                    is FirebaseResult.Error -> {
                        _event.trySend(
                            AllProductsEvent.Error(
                                getCartResponse.exception.message ?: "Error updating the cart",
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun loadProducts() {
        val productCartFlow =
            userData
                .getCartId()
                .distinctUntilChanged()
                .flatMapLatest { cartId ->
                    if (cartId == null) {
                        flowOf(emptyMap())
                    } else {
                        cartRepository
                            .getCart(cartId)
                            .map { result ->
                                when (result) {
                                    is FirebaseResult.Success -> {
                                        result.data.associate { cartItem ->
                                            val productId = cartItem.reference.substringAfterLast("/")
                                            productId to cartItem.quantity
                                        }
                                    }

                                    is FirebaseResult.Error -> {
                                        _event.trySend(
                                            AllProductsEvent.Error(
                                                result.exception.message ?: "Error getting the cart",
                                            ),
                                        )
                                        emptyMap()
                                    }
                                }
                            }
                    }
                }.distinctUntilChanged()

        combine(
            productsRepository
                .getAllProducts()
                .distinctUntilChanged(),
            productCartFlow,
        ) { products, quantities ->
            val cartItemsUI =
                products.map { product ->
                    val quantity = quantities[product.id] ?: 0
                    CartItemUI(
                        reference = "${product.type.name.lowercase()}s/${product.id}",
                        name = product.name,
                        image = product.image,
                        price = product.price,
                        type = product.type,
                        quantity = quantity,
                        ingredients =
                            if (product.type == ProductType.PIZZA) {
                                (product as Product.Pizza).ingredients
                            } else {
                                emptyList()
                            },
                    )
                }

            val cartItemsUIGrouped =
                cartItemsUI
                    .sortedBy { it.type.ordinal }
                    .groupBy { it.type }

            var index = 0
            val headerIndexMap =
                cartItemsUIGrouped.entries.associate { (type, items) ->
                    val currentIndex = index
                    index += items.size + 1
                    type to currentIndex
                }

            Pair(
                cartItemsUIGrouped,
                headerIndexMap,
            )
        }.onEach { (grouped, headerIndexMap) ->
            _state.update {
                it.copy(
                    products = grouped,
                    productsFiltered = grouped,
                    headerIndexMap = headerIndexMap,
                    isCreateNewCart = false,
                    isLoading = false,
                )
            }
        }.launchIn(viewModelScope)
    }
}
