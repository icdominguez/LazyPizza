package com.seno.auth.presentation.login

import com.seno.core.presentation.utils.UiText

data class LoginState(
    val phoneNumber: String = "",
    val isContinueButtonEnabled: Boolean = false,
    val otp: String = "",
    val isCodeSent: Boolean = false,
    val isLoading: Boolean = false,
    val timeLeft: Int = 60,
    val isTimerRunning: Boolean = false,
    val error: UiText? = null
)
