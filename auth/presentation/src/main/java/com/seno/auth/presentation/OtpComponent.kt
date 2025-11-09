package com.seno.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_2_regular
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.surfaceHighest
import com.seno.core.presentation.theme.textPrimary

private const val OTP_LENGTH = 4

@Composable
fun OtpComponent(
    otp: String,
    onOptTextChange: (String) -> Unit = {},
    isError: Boolean = false
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = otp,
        singleLine = true,
        onValueChange = {
            if(it.length <= OTP_LENGTH) {
                onOptTextChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        ),
        decorationBox = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                repeat(OTP_LENGTH) { index ->
                    val char = when {
                        index >= otp.length -> ""
                        else -> otp[index].toString()
                    }
                    Text(
                        modifier = Modifier
                            .background(
                                color = if (isError) Color.Transparent else surfaceHighest,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if(isError) primary else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(
                                vertical = 12.dp,
                                horizontal = 20.dp
                            ),
                        text = char,
                        style = body_2_regular,
                        color = textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun OptComponentPreview() {
    LazyPizzaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            OtpComponent(
                otp = "1234",
                onOptTextChange = {},
            )

            OtpComponent(
                otp = "1234",
                onOptTextChange = {},
                isError = true,
            )
        }
    }
}