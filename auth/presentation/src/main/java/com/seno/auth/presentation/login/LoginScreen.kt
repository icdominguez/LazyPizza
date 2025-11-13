package com.seno.auth.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.auth.presentation.OtpComponent
import com.seno.core.presentation.components.DefaultLazyPizzaTextField
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_medium
import com.seno.core.presentation.theme.title_3
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {},
) {
    var timeLeft by remember { mutableStateOf(60) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(state.isCodeSent) {
        if (state.isCodeSent) {
            isTimerRunning = true
            timeLeft = 60

            while (timeLeft > 0 && isTimerRunning) {
                delay(1000)
                timeLeft--
            }
            isTimerRunning = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome to LazyPizza",
            style = title_1_medium,
            color = textPrimary,
        )

        Spacer(modifier = Modifier.size(6.dp))
        if (state.isCodeSent) {
            Text(
                text = "Enter code",
                style = body_3_regular,
                color = textSecondary,
            )
        } else {
            Text(
                text = "Enter your phone number",
                style = body_3_regular,
                color = textSecondary,
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        DefaultLazyPizzaTextField(
            value = state.phoneNumber,
            onValueChange = {
                onAction(LoginAction.OnPhoneNumberChange(it))
            },
            placeholder = "+1000 00 0000",
            isPhoneNumber = true,
            supportingText = "Phone number can not have more than 15 digits",
        )

        Spacer(modifier = Modifier.size(16.dp))

        AnimatedVisibility(visible = state.isCodeSent) {
            OtpComponent(
                otp = state.otp,
                onOptTextChange = {
                    onAction(LoginAction.OnOtpChange(it))
                },
                isError = state.isWrongOtp,
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        if (state.isCodeSent) {
            LazyPizzaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                buttonText = "Confirm",
                onClick = {
                    onAction(LoginAction.OnOtpConfirm)
                },
                enabled = state.otp.length == 6 && !state.isLoading
            )
        } else {
            LazyPizzaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                buttonText = "Continue",
                onClick = {
                    onAction(LoginAction.OnContinueButtonClick)
                },
                enabled = state.isContinueButtonEnabled,
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            modifier = Modifier
                .clickable(
                    onClick = {
                        onAction(LoginAction.OnContinueWithoutSignInClick)
                    },
                ),
            text = "Continue without signing in",
            style = title_3,
            color = primary,
        )
        Spacer(modifier = Modifier.size(12.dp))

        if (state.isCodeSent) {
            if (isTimerRunning && timeLeft > 0) {
                val minutes = timeLeft / 60
                val seconds = timeLeft % 60
                Text(
                    text = "You can request a new code in ${String.format("%02d:%02d", minutes, seconds)}",
                    style = body_3_regular.copy(textSecondary),
                )
            } else {
                TextButton(
                    onClick = {
                        onAction(LoginAction.OnOtpResend)
                    }
                ) {
                    Text(
                        text = "Resend code",
                        style = title_3.copy(primary),
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun LoginScreenPreview() {
    LazyPizzaTheme {
        LoginScreen()
    }
}
