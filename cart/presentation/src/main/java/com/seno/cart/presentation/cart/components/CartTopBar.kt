package com.seno.cart.presentation.cart.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.seno.cart.presentation.R
import com.seno.core.presentation.components.bar.LazyPizzaTopAppBar
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_1_medium

@Composable
fun CartTopBar(modifier: Modifier = Modifier) {
    LazyPizzaTopAppBar(
        modifier = modifier,
        isCentered = true,
        title = {
            Text(
                text = stringResource(R.string.cart),
                style = body_1_medium,
                modifier =
                    Modifier
                        .fillMaxWidth(),
                textAlign = TextAlign.Center,
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
