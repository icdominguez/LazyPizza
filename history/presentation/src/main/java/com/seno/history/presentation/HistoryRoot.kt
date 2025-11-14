package com.seno.history.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryRoot(
    viewModel: HistoryViewModel = koinViewModel(),
    onSignInClick: () -> Unit = {},
    onGotoMenuClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
    }

    HistoryScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is HistoryAction.OnSingInClick -> onSignInClick()
                is HistoryAction.OnGoToMenuClick -> onGotoMenuClick()
                else -> viewModel.onAction(action)
            }
        },
    )
}
