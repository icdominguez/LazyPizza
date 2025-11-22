package com.seno.history.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.seno.core.presentation.components.bar.LazyPizzaTopAppBar
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_1_medium
import com.seno.history.presentation.R

@Composable
fun HistoryTopBar(modifier: Modifier = Modifier) {
    LazyPizzaTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.order_history),
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
        HistoryTopBar()
    }
}
