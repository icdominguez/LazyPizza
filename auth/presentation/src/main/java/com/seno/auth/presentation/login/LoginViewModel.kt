package com.seno.auth.presentation.login

import androidx.lifecycle.ViewModel
import com.seno.auth.domain.PhoneValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val phoneValidator: PhoneValidator
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPhoneNumberChange -> onPhoneNumberChange(action.phoneNumber)
            else -> Unit
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) {
        _state.update { currentState ->
            currentState.copy(
                phoneNumber = phoneNumber,
            )
        }

        if (phoneNumber.length > 8 && phoneNumber.length < 15) {
            val isValidPhone = phoneValidator.isValid(phoneNumber)
            _state.update { currentState ->
                currentState.copy(
                    isContinueButtonEnabled = isValidPhone,
                )
            }
        } else {
            _state.update { currentState ->
                currentState.copy(
                    isContinueButtonEnabled = false,
                )
            }
        }
    }
}
