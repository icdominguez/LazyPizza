package com.seno.lazypizza

import com.seno.products.presentation.allproducts.LoginState

data class MainState(
    val totalCartItem: Int = 0,
    val loginState: LoginState = LoginState.LoggedOut,
    val dialogState: Boolean = false
)
