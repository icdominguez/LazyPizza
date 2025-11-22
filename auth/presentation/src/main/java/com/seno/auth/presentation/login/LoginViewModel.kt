package com.seno.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.auth.domain.PhoneValidator
import com.seno.auth.domain.repository.AuthService
import com.seno.core.domain.onError
import com.seno.core.domain.onSuccess
import com.seno.core.domain.userdata.UserData
import com.seno.core.presentation.utils.asUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val phoneValidator: PhoneValidator,
    private val authService: AuthService,
    private val userData: UserData,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        _state
            .map { it.isTimerRunning }
            .distinctUntilChanged()
            .onEach { running ->
                if (running) {
                    startTimer()
                }
            }.launchIn(viewModelScope)
    }

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
            _state.update { it.copy(isLoading = true) }

            authService
                .sendCode(_state.value.phoneNumber)
                .onSuccess { requestId ->
                    savedStateHandle[REQUEST_ID_KEY] = requestId
                    savedStateHandle[PHONE_NUMBER_KEY] = _state.value.phoneNumber
                    _state.update {
                        it.copy(
                            isCodeSent = true,
                            isTimerRunning = true,
                            isLoading = false,
                        )
                    }
                }.onError { error ->
                    _event.send(LoginEvent.LoginError(error.asUiText()))
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isCodeSent = false,
                        )
                    }
                }
        }
    }

    private fun onOtpChange(newOtp: String) {
        _state.update {
            it.copy(
                otp = newOtp,
            )
        }
    }

    private fun onOtpConfirm() {
        val currentOtp = state.value.otp
        val requestId = savedStateHandle.get<String>(REQUEST_ID_KEY)
        val phoneNumber = savedStateHandle.get<String>(PHONE_NUMBER_KEY)

        if (currentOtp.length != 6 || requestId == null || phoneNumber == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authService
                .verificationCode(
                    requestId = requestId,
                    code = currentOtp,
                ).onSuccess {
                    userData.setUserId(phoneNumber)
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _event.send(LoginEvent.LoginSuccess)
                }.onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.asUiText(),
                        )
                    }
                }
        }
    }

    private fun onOtpResend() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }

            authService
                .sendCode(state.value.phoneNumber)
                .onSuccess { requestId ->
                    savedStateHandle[REQUEST_ID_KEY] = requestId
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            otp = "",
                            isTimerRunning = true,
                        )
                    }
                }.onError { error ->
                    _event.send(LoginEvent.LoginError(error.asUiText()))
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val currentTimeLeft = _state.value.timeLeft
                delay(1000)
                if (currentTimeLeft > 0) {
                    _state.update { it.copy(timeLeft = currentTimeLeft - 1) }
                } else {
                    _state.update { it.copy(isTimerRunning = false, timeLeft = 60) }
                    timerJob?.cancel()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object {
        private const val REQUEST_ID_KEY = "request_id"
        private const val PHONE_NUMBER_KEY = "phone_number"
    }
}
