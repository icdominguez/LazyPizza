package com.seno.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_2_regular
import com.seno.core.presentation.theme.body_4_regular
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.surfaceHighest
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary

private const val OTP_LENGTH = 6

@Composable
fun OtpComponent(
    otp: String,
    onOptTextChange: (String) -> Unit = {},
    error: String? = null,
) {
    var containerWidthPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val otpItemsSpacing = 8.dp
    val otpItemsSpacingPx = with(density) { otpItemsSpacing.roundToPx() }

    val opItemWidth = (containerWidthPx - otpItemsSpacingPx * (OTP_LENGTH - 1)) / OTP_LENGTH

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                containerWidthPx = coordinates.size.width
            },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = otp,
            singleLine = true,
            onValueChange = {
                if (it.length <= OTP_LENGTH) {
                    onOptTextChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
            decorationBox = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        otpItemsSpacing,
                        Alignment.CenterHorizontally,
                    ),
                ) {
                    repeat(OTP_LENGTH) { index ->
                        val char = when {
                            index >= otp.length -> ""
                            else -> otp[index].toString()
                        }
                        Box(
                            modifier = Modifier
                                .width(
                                    with(density) {
                                        opItemWidth.toDp()
                                    },
                                ).height(48.dp)
                                .background(
                                    color = if (error != null) Color.Transparent else surfaceHighest,
                                    shape = RoundedCornerShape(16.dp),
                                ).border(
                                    width = 1.dp,
                                    color = if (error != null) primary else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (char.isNotEmpty()) {
                                Text(
                                    text = char,
                                    style = body_2_regular,
                                    color = textPrimary,
                                    textAlign = TextAlign.Center,
                                )
                            } else {
                                Text(
                                    text = "0",
                                    style = body_2_regular,
                                    color = textSecondary,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            },
        )
        error?.let {
            Text(
                text = it,
                style = body_4_regular.copy(color = primary),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OptComponentPreview() {
    LazyPizzaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OtpComponent(
                otp = "123456",
                onOptTextChange = {},
            )

            OtpComponent(
                otp = "123456",
                onOptTextChange = {},
                error = "Error message",
            )
        }
    }
}
