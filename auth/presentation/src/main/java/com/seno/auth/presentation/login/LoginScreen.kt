package com.seno.auth.presentation.login

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.components.DefaultLazyPizzaTextField
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_medium
import com.seno.core.presentation.theme.title_3

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {},
) {
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

        Text(
            text = "Enter your phone number",
            style = body_3_regular,
            color = textSecondary,
        )

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

        LazyPizzaPrimaryButton(
            modifier = Modifier
                .fillMaxWidth(),
            buttonText = "Continue",
            onClick = {
                onAction(LoginAction.OnContinueButtonClick)
            },
            enabled = state.isContinueButtonEnabled,
        )

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
