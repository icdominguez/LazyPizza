package com.seno.cart.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.cart.presentation.R
import com.seno.core.presentation.components.bar.LazyPizzaTopAppBar
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_1_medium
import com.seno.core.presentation.utils.DeviceConfiguration

@Composable
fun CartTopBar(
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    LazyPizzaTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.cart),
                style = body_1_medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = if (deviceType.isTablet()) 50.dp else 0.dp)
                    .offset(x = if (!deviceType.isTablet()) (-8).dp else 0.dp),
                textAlign = TextAlign.Center
            )
        },
    )
}

@Preview
@Composable
private fun HistoryTopBarPreview() {
    LazyPizzaTheme {
        CartTopBar()
    }
}