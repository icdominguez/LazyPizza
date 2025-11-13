package com.seno.auth.presentation.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onContinueWithoutSignIn: () -> Unit = {},
    onConfirmButtonClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val defaultPadding = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE -> 16.dp

        else -> 220.dp
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                LoginEvent.LoginSuccess -> onConfirmButtonClicked()
                LoginEvent.LoginError -> {}
            }
        }
    }

    Scaffold {
        LoginScreen(
            modifier = Modifier
                .padding(it)
                .padding(defaultPadding),
            state = state,
            onAction = { action ->
                when (action) {
                    is LoginAction.OnContinueWithoutSignInClick -> onContinueWithoutSignIn()
                    is LoginAction.OnOtpConfirm -> viewModel.onAction(action)
                    else -> viewModel.onAction(action)
                }
            },
        )
    }
}
