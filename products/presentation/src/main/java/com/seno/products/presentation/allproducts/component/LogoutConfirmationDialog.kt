package com.seno.products.presentation.allproducts.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.seno.core.presentation.R
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.primaryGradient
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.title_1_medium
import com.seno.core.presentation.theme.title_3

@Composable
fun LogoutConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Card(
            modifier = Modifier
                .width(360.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.confirmation_dialog_title),
                    modifier = Modifier.padding(16.dp),
                    style = title_1_medium,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = title_3.copy(
                                color = primary,
                            ),
                        )
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                brush = primaryGradient,
                                shape = CircleShape,
                            ),
                    ) {
                        Text(
                            text = stringResource(R.string.logout),
                            style = title_3.copy(
                                color = textOnPrimary,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogoutConfirmationDialogPreview() {
    LogoutConfirmationDialog(
        onDismissRequest = {},
        onConfirmation = {},
    )
}
