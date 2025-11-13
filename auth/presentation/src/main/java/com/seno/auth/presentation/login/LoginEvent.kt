package com.seno.auth.presentation.login

import com.seno.core.presentation.utils.UiText

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
    data class LoginError(val message: UiText) : LoginEvent
}
