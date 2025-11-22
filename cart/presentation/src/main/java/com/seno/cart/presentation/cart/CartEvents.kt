package com.seno.cart.presentation.cart

sealed interface CartEvents {
    data class Error(val error: String) : CartEvents
}
