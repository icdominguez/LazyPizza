package com.seno.auth.presentation.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
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
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val defaultPadding = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE -> 16.dp
        else -> 220.dp
    }

    LoginScreen(
        modifier = Modifier
            .padding(defaultPadding),
        state = state,
        onAction = { action ->
            when (action) {
                is LoginAction.OnContinueWithoutSignInClick -> onContinueWithoutSignIn()
                else -> viewModel.onAction(action)
            }
        },
    )
}
