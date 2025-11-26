package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.components.bar.LazyPizzaTopAppBar
import com.seno.core.presentation.theme.body_1_medium
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.textSecondary8
import com.seno.core.presentation.utils.DeviceConfiguration

@Composable
fun OrderCheckoutTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    LazyPizzaTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(com.seno.cart.presentation.R.string.order_checkout),
                style = body_1_medium,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = if (deviceType.isTablet()) 50.dp else 0.dp)
                        .offset(x = if (!deviceType.isTablet()) (-8).dp else 0.dp),
                textAlign = TextAlign.Center,
            )
        },
        navigationIcon = {
            IconButton(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .size(32.dp)
                        .background(
                            color = textSecondary8.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(100),
                        ),
                onClick = { onBackClick() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "back",
                    tint = textSecondary,
                )
            }
        },
    )
}
