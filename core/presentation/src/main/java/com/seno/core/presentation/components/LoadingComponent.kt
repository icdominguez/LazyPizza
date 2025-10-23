package com.seno.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_1_medium
import com.seno.core.presentation.theme.primary

@Composable
fun LoadingComponent(
    text: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            style = body_1_medium.copy(
                color = primary
            ),
        )
    }
}

@Preview
@Composable
private fun LoadingComponentPreview() {
    LazyPizzaTheme {
        LoadingComponent(text = "Getting your cart, please wait ...")
    }
}