package com.seno.products.presentation.allproducts

sealed interface LoginState {
    object LoggedIn : LoginState
    object LoggedOut : LoginState
    object ShowLogoutDialog: LoginState
    object DismissLogoutDialog: LoginState
}