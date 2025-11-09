package com.seno.auth.presentation.login

data class LoginState(
    val phoneNumber: String = "",
    val isContinueButtonEnabled: Boolean = false,
    val otp: String = "",
    val isCodeSent: Boolean = false,
    val isWrongOtp: Boolean = false,
)
