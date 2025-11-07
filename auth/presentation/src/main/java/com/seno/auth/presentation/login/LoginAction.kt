package com.seno.auth.presentation.login

interface LoginAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : LoginAction

    object OnContinueWithoutSignInClick : LoginAction

    object OnContinueButtonClick : LoginAction
}
