package com.seno.auth.presentation.login

interface LoginAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : LoginAction

    data class OnOtpChange(val newOtp: String): LoginAction

    data object OnOtpConfirm: LoginAction

    data object OnOtpResend: LoginAction

    object OnContinueWithoutSignInClick : LoginAction

    object OnContinueButtonClick : LoginAction
}
