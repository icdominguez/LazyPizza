package com.seno.products.presentation.allproducts.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.R
import com.seno.core.presentation.components.bar.LazyPizzaTopAppBar
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_1_regular
import com.seno.core.presentation.theme.body_3_body
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.products.presentation.allproducts.LoginState

@Composable
fun AllProductsTopBar(
    modifier: Modifier = Modifier,
    loginState: LoginState,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    LazyPizzaTopAppBar(
        title = {
            Row(
                modifier =
                    modifier
                        .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier =
                        Modifier
                            .size(20.dp)
                            .graphicsLayer(scaleX = 1.8f, scaleY = 1.8f),
                    painter = painterResource(id = R.drawable.logo),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Icon pizza",
                )

                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = "LazyPizza",
                    style = body_3_body,
                )
            }
        },
        actions = {
            Row(
                modifier =
                    Modifier
                        .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier =
                        Modifier
                            .size(14.dp),
                    painter = painterResource(R.drawable.phone_ic),
                    contentDescription = "Icon phone",
                    tint = textSecondary,
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = "+1 (555) 321-7890",
                    style = body_1_regular,
                    color = textPrimary,
                )

                Spacer(modifier = Modifier.size(16.dp))
            }
            when (loginState) {
                LoginState.LoggedOut -> {
                    IconButton(
                        onClick = onLoginClick,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.profile_ic),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }
                LoginState.LoggedIn -> {
                    IconButton(
                        onClick = onLogoutClick,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.logout_ic),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                    }
                }
                else -> Unit
            }
        },
    )
}

@Preview
@Composable
private fun AllProductsTopBarPreview() {
    LazyPizzaTheme {
        AllProductsTopBar(
            loginState = LoginState.LoggedOut
        )
    }
}
