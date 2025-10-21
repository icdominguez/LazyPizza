package com.seno.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_medium
import com.seno.core.presentation.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->

    }

    HistoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 120.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.not_signed_in),
            style = title_1_medium,
            color = textPrimary
        )
        Text(
            text = stringResource(R.string.please_sign_in_to_view_your_order_history),
            style = body_3_regular,
            color = textSecondary
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        LazyPizzaPrimaryButton(
            buttonText = stringResource(R.string.sign_in),
            onClick = {

            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(),
            onAction = {}
        )
    }
}