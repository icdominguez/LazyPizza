package com.seno.core.presentation.components

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import com.seno.core.presentation.theme.success
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.utils.ObserveAsEvents
import com.seno.core.presentation.utils.SnackbarController
import kotlinx.coroutines.launch

@Composable
fun LazyPizzaDefaultScreen(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    topAppBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(
        SnackbarController.events,
        snackbarHostState,
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result =
                snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.name,
                    withDismissAction = event.onDismiss != null,
                    duration = SnackbarDuration.Short,
                )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        it,
                        containerColor = MaterialTheme.colorScheme.success,
                        contentColor = textPrimary,
                        actionContentColor = textPrimary,
                        dismissActionContentColor = textPrimary,
                        actionColor = textPrimary,
                        shape = RoundedCornerShape(8.dp),
                    )
                },
            )
        },
        modifier = modifier,
        topBar = topAppBar,
        containerColor = containerColor,
    ) { innerPadding ->
        val view = LocalView.current

        Box(
            modifier =
                Modifier
                    .padding(innerPadding),
        ) {
            content()
        }

        SideEffect {
            val window = (view.context as? Activity)?.window
            if (!view.isInEditMode && window != null) {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    true
            }
        }
    }
}
