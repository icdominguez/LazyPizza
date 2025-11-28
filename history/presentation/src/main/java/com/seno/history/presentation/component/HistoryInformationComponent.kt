package com.seno.history.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_medium
import com.seno.history.presentation.R

@Composable
fun HistoryInformationComponent(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .height(IntrinsicSize.Min)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = title_1_medium,
            color = textPrimary,
        )
        Text(
            text = description,
            style = body_3_regular,
            textAlign = TextAlign.Center,
            color = textSecondary,
        )
        Spacer(
            modifier = Modifier.height(16.dp),
        )
        LazyPizzaPrimaryButton(
            buttonText = buttonText,
            onClick = { onClick() },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateUIPreview() {
    LazyPizzaTheme {
        HistoryInformationComponent(
            title = stringResource(R.string.not_signed_in),
            description = stringResource(R.string.please_sign_in_to_view_your_order_history),
            buttonText = stringResource(R.string.sign_in),
        )
    }
}
