package com.seno.products.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.products.domain.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    productsRepository: ProductsRepository
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