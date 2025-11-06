package com.seno.lazypizza

data class MainState(
    val totalCartItem: Int = 0,
    val isLoggedIn: Boolean = false,
    val showLogoutDialog: Boolean = false,
)
