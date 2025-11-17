package com.seno.history.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seno.core.presentation.utils.ObserveAsEvents
import com.seno.history.presentation.component.HistoryTopBar
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

    Scaffold(
        topBar = {
            HistoryTopBar()
        },
    ) { innerPadding ->
        HistoryScreen(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
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
}
