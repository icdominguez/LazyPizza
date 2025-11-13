package com.seno.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.auth.domain.PhoneValidator
import com.seno.auth.domain.repository.AuthService
import com.seno.core.domain.onError
import com.seno.core.domain.onSuccess
import com.seno.core.domain.userdata.UserData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val phoneValidator: PhoneValidator,
    private val authService: AuthService,
    private val userData: UserData
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPhoneNumberChange -> onPhoneNumberChange(action.phoneNumber)
            is LoginAction.OnContinueButtonClick -> onContinueButtonClick()
            is LoginAction.OnOtpChange -> onOtpChange(action.newOtp)
            is LoginAction.OnOtpResend -> onOtpResend()
            is LoginAction.OnOtpConfirm -> onOtpConfirm()
            else -> Unit
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) {
        _state.update { currentState ->
            currentState.copy(
                phoneNumber = phoneNumber,
            )
        }

        if (phoneNumber.length in 9..<15) {
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

    private fun onContinueButtonClick() {
        viewModelScope.launch {
            _state.update { it.copy(isCodeSent = true) }

            try {
                val result = authService.sendCode(state.value.phoneNumber)

                result.onSuccess { requestId ->
                    _state.update { it.copy(
                        isCodeSent = true,
                        requestId = requestId,
                    )}
                }.onError { error ->
                    _state.update { it.copy(
                        isCodeSent = false,
                    )}
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isCodeSent = false,
                )}
            }
        }
    }

    private fun onOtpChange(newOtp: String) {
        _state.update { it.copy(
            otp = newOtp,
            isWrongOtp = false
        )}
    }

    private fun onOtpConfirm() {
        val currentOtp = state.value.otp
        val requestId = state.value.requestId

        if (currentOtp.length != 6 || requestId == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val result = authService.verificationCode(
                    requestId = requestId,
                    code = currentOtp
                )

                result.onSuccess {
                    userData.setIsLogin(true)
                    _state.update { it.copy(
                        isLoggedIn = true,
                        isValidOtp = true,
                        isLoading = false
                    )}
                    _event.send(LoginEvent.LoginSuccess)
                }.onError { error ->
                    _state.update { it.copy(
                        isValidOtp = false,
                        isWrongOtp = true,
                        isLoading = false
                    )}

                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isValidOtp = false,
                    isWrongOtp = true,
                    isLoading = false
                )}
            }
        }
    }

    private fun onOtpResend() {
        viewModelScope.launch {
            _state.update { it.copy(isCodeSent = false) }

            try {
                val result = authService.sendCode(state.value.phoneNumber)

                result.onSuccess { requestId ->
                    _state.update { it.copy(
                        isCodeSent = true,
                        requestId = requestId,
                        otp = "",
                        isValidOtp = false
                    )}
                }.onError { error ->
                    _state.update { it.copy(
                        isCodeSent = true,
                    )}
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isCodeSent = true,
                )}
            }
        }
    }
}
