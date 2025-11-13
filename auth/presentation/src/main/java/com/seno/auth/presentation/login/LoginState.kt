package com.seno.auth.presentation.login

import com.seno.auth.domain.model.RequestId

data class LoginState(
    val isLoggedIn: Boolean = false,
    val phoneNumber: String = "",
    val isContinueButtonEnabled: Boolean = false,
    val otp: String = "",
    val requestId: RequestId? = null,
    val isCodeSent: Boolean = false,
    val isWrongOtp: Boolean = false,
    val isValidOtp: Boolean = false,
    val isLoading: Boolean = false,
    val timeLeft: Int = 60,
    val isTimerRunning: Boolean = false,


    )
