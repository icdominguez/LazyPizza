package com.seno.cart.presentation

sealed interface CartEvents {
    data class Error(val error: String) : CartEvents
}
