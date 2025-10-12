package com.seno.core.presentation.components

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun LazyPizzaDefaultScreen(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    topAppBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = topAppBar,
        containerColor = containerColor,
    ) { innerPadding ->
        val view = LocalView.current

        Box(
            modifier = modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                ),
        ) {
            content()
        }

        SideEffect {
            val window = (view.context as? Activity)?.window
            if (!view.isInEditMode && window != null) {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
            }
        }
    }
}