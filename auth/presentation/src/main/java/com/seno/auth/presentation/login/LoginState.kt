package com.seno.auth.presentation.login

data class LoginState(
    val phoneNumber: String = "",
    val isContinueButtonEnabled: Boolean = false,
)
