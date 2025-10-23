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
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class AllProductsViewModel(
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val userData: UserData,
) : ViewModel() {
    private val _state = MutableStateFlow(AllProductsState())
    val state = _state.asStateFlow()

    private val _event = Channel<AllProductsEvent>()
    val event = _event.receiveAsFlow()

    init {
        loadProducts()
    }

    fun onAction(action: AllProductsAction) {
        when (action) {
            is AllProductsAction.OnQueryChange -> {
                val productsFiltered = if (action.newSearchQuery.isNotEmpty()) {
                    _state.value.products.mapValues { (_, products) ->
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
                        productsFiltered = productsFiltered
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
            userData.getCartId().collect { cartId ->
                if (cartId == null) {
                    val createCartResponse = cartRepository.createCart(items = listOf(product.toCartItem()))
                    when (createCartResponse) {
                        is FirebaseResult.Success -> {
                            userData.setCardId(createCartResponse.data)
                        }
                        is FirebaseResult.Error -> {
                            _event.trySend(AllProductsEvent.Error(createCartResponse.exception.message ?: "Error creating the cart"))
                        }
                    }
                } else {
                    val getCartResponse = cartRepository.getCart(cartId).first()

                    when (getCartResponse) {
                        is FirebaseResult.Success -> {
                            val updatedCartItems = getCartResponse.data.toMutableList().apply {
                                val cartItemSelectedIndex = this.indexOf(cartItemSelected)
                                if (cartItemSelectedIndex == -1) {
                                    this.add(cartItemSelected)
                                } else {
                                    this[cartItemSelectedIndex] =
                                        this[cartItemSelectedIndex].copy(quantity = product.quantity + 1)
                                }
                            }
                            cartRepository.updateCart(
                                cartId = cartId,
                                items = updatedCartItems
                            )
                        }

                        is FirebaseResult.Error -> {
                            _event.trySend(AllProductsEvent.Error(getCartResponse.exception.message ?: "Error updating the cart"))
                        }
                    }
                }
            }
        }
    }

    private fun onProductMinus(product: CartItemUI) {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItemSelected = product.copy(quantity = product.quantity - 1).toCartItem()
            userData.getCartId().collect { cartId ->
                if (cartId == null) {
                    cartRepository.createCart(items = listOf(product.toCartItem()))
                } else {
                    val getCartResponse = cartRepository.getCart(cartId).first()

                    when (getCartResponse) {
                        is FirebaseResult.Success -> {
                            val updatedCartItems = getCartResponse.data.toMutableList().apply {
                                val cartItemSelectedIndex = this.indexOf(cartItemSelected)
                                if (cartItemSelectedIndex == -1) {
                                    this.add(cartItemSelected)
                                } else {
                                    this[cartItemSelectedIndex] =
                                        this[cartItemSelectedIndex].copy(quantity = product.quantity - 1)
                                }
                            }
                            cartRepository.updateCart(
                                cartId = cartId,
                                items = updatedCartItems
                            )
                        }

                        is FirebaseResult.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun onProductDelete(product: CartItemUI) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val cartItemToDelete = product.toCartItem()

            userData.getCartId().collect { cartId ->
                cartId?.let {
                    val getCartResponse = cartRepository.getCart(cartId).first()

                    when (getCartResponse) {
                        is FirebaseResult.Success -> {
                            val updatedCartItems = getCartResponse
                                .data
                                .filter { it.reference != cartItemToDelete.reference }

                            cartRepository.updateCart(
                                cartId = cartId,
                                items = updatedCartItems
                            )
                        }

                        is FirebaseResult.Error -> {
                            Timber.tag("ICD")
                                .e("There was an error deleting the cartItem: $cartItemToDelete")
                        }
                    }
                }
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch(context = Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            val products = productsRepository.getAllProducts().first()
            userData.getCartId().collect { cartId ->
                if(cartId == null) {
                    val cartItemsUI = products.map { product ->
                        CartItemUI(
                            reference = "${product.type.name.lowercase()}s/${product.id}",
                            name = product.name,
                            image = product.image,
                            price = product.price,
                            type = product.type,
                            ingredients = if (product.type == ProductType.PIZZA) {
                                (product as Product.Pizza).ingredients
                            } else {
                                emptyList()
                            },
                        )
                    }

                    // 4. Group the cartItemsUI to have the sticky headers
                    val cartItemsUIGrouped = cartItemsUI
                        .sortedBy { it.type.ordinal }
                        .groupBy { it.type }

                    var index = 0
                    val headerIndexMap =
                        cartItemsUIGrouped.entries.associate { (type, items) ->
                            val currentIndex = index
                            index += items.size + 1
                            type to currentIndex
                        }

                    cartItemsUIGrouped to headerIndexMap

                    _state.update {
                        it.copy(
                            products = cartItemsUIGrouped,
                            productsFiltered = cartItemsUIGrouped,
                            headerIndexMap = headerIndexMap,
                            isLoading = false
                        )
                    }
                } else {
                    cartRepository.getCart(cartId).collect { cartRepositoryResponse ->
                        when (cartRepositoryResponse) {
                            is FirebaseResult.Success -> {
                                // 1. Get the cart items
                                val cartItems = cartRepositoryResponse.data

                                // 2. Assign quantity to each product id
                                val quantitiesById = cartItems.associate { cartItem ->
                                    val productId = cartItem.reference.substringAfterLast("/")
                                    productId to cartItem.quantity
                                }

                                // 3. Get the cart items UI with its corresponding quantity
                                val cartItemsUI = products.map { product ->
                                    val quantity = quantitiesById[product.id] ?: 0
                                    CartItemUI(
                                        reference = "${product.type.name.lowercase()}s/${product.id}",
                                        name = product.name,
                                        image = product.image,
                                        price = product.price,
                                        type = product.type,
                                        quantity = quantity,
                                        ingredients = if (product.type == ProductType.PIZZA) {
                                            (product as Product.Pizza).ingredients
                                        } else {
                                            emptyList()
                                        },
                                    )
                                }

                                // 4. Group the cartItemsUI to have the sticky headers
                                val cartItemsUIGrouped = cartItemsUI
                                    .sortedBy { it.type.ordinal }
                                    .groupBy { it.type }

                                var index = 0
                                val headerIndexMap =
                                    cartItemsUIGrouped.entries.associate { (type, items) ->
                                        val currentIndex = index
                                        index += items.size + 1
                                        type to currentIndex
                                    }

                                cartItemsUIGrouped to headerIndexMap

                                _state.update {
                                    it.copy(
                                        products = cartItemsUIGrouped,
                                        productsFiltered = cartItemsUIGrouped,
                                        headerIndexMap = headerIndexMap,
                                        isLoading = false
                                    )
                                }
                            }

                            is FirebaseResult.Error -> {
                                _event.trySend(AllProductsEvent.Error(cartRepositoryResponse.exception.message ?: "Error getting the cart"))
                            }
                        }
                    }
                }
            }

        }
    }
}