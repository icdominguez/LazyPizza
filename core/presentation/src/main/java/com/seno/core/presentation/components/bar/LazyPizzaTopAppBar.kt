package com.seno.core.presentation.components.bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.components.button.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyPizzaTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    isCentered: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart),
        ) {
            navigationIcon()
        }

        Box(
            modifier = Modifier
                .align(
                    if (isCentered) {
                        Alignment.Center
                    } else {
                        Alignment.CenterStart
                    },
                ),
        ) {
            title()
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
        ) {
            actions()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LazyPizzaTopAppBarPreview() {
    LazyPizzaTopAppBar(
        navigationIcon = {
            BackButton { }
        },
    )
}
