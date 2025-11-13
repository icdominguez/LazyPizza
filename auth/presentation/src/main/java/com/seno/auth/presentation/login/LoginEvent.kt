package com.seno.auth.presentation.login

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
    data object LoginError : LoginEvent
}
