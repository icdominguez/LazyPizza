package com.seno.products.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.cart.domain.CartRepository
import com.seno.core.domain.cart.CartItem
import com.seno.core.domain.userdata.UserData
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val userData: UserData,
) : ViewModel() {

    private val pizzaName = savedStateHandle.get<String>(PIZZA_NAME) ?: ""

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    init {
        combine(
            productsRepository.getPizzaByName(pizzaName),
            productsRepository.getExtraToppingsFlow()
        ) { pizza, toppings ->
            _state.update {
                it.copy(
                    selectedPizza = pizza,
                    listExtraToppings = toppings.map { product ->
                        ToppingsUI(
                            id = product.id,
                            name = product.name,
                            image = product.image,
                            price = product.price,
                            quantity = 0
                        )
                    }
                )
            }
        }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onAction(action: ProductDetailAction) {
        when (action) {
            is ProductDetailAction.OnToppingPlus -> onToppingPlus(action.toppingsUI)
            is ProductDetailAction.OnToppingMinus -> onToppingMinus(action.toppingsUI)
            is ProductDetailAction.OnAddToCartButtonClick -> onAddToCartButtonClick()
        }
    }

    private fun onAddToCartButtonClick() {
        val cartItems = _state.value.listExtraToppings
            .filterNot { it.quantity == 0 }
            .map {
                CartItem(
                    reference = "${it.type.name.lowercase()}/${it.id}",
                    quantity = it.quantity
                )
            }
            .toMutableList()
            .apply {
                _state.value.selectedPizza?.let { pizza ->
                    add(
                        CartItem(
                            reference = "${pizza.type.name.lowercase()}/${pizza.id}",
                            quantity = 1
                        )
                    )
                }
            }

        viewModelScope.launch(context = Dispatchers.IO) {
            // TODO: Check if dataStore.getCardId() is null. If null: cartRepository.createCart() else cartRepository.updateCart()
        }
    }

    fun onToppingPlus(toppingsUI: ToppingsUI) {
        val updatedTopping = _state.value.listExtraToppings.map {
            if (it == toppingsUI) {
                it.copy(quantity = it.quantity + 1)
            } else {
                it
            }
        }
        val updatedPizza = _state.value.selectedPizza?.let {
            it.copy(
                price = it.price + toppingsUI.price
            )
        }

        _state.update {
            it.copy(
                selectedPizza = updatedPizza,
                listExtraToppings = updatedTopping
            )
        }
    }

    fun onToppingMinus(toppingsUI: ToppingsUI) {
        val updatedTopping = _state.value.listExtraToppings.map {
            if (it == toppingsUI) {
                it.copy(quantity = it.quantity - 1)
            } else {
                it
            }
        }

        val updatedPizza = _state.value.selectedPizza?.let {
            it.copy(
                price = it.price - toppingsUI.price
            )
        }

        _state.update {
            it.copy(
                selectedPizza = updatedPizza,
                listExtraToppings = updatedTopping
            )
        }
    }

    companion object {
        private const val PIZZA_NAME = "pizzaName"
    }
}