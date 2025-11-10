package com.seno.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.body_2_regular
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.surfaceHighest
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DefaultLazyPizzaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPhoneNumber: Boolean = false,
    supportingText: String = "",
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    var textState by remember { mutableStateOf(TextFieldValue(value)) }
    var shouldShowSupportingText by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val onValueChangeState = rememberUpdatedState(onValueChange)

    LaunchedEffect(isFocused) {
        if (isFocused && !value.startsWith("+")) {
            val newText = "+${textState.text}"
            textState = TextFieldValue(
                text = newText,
                selection = TextRange(newText.length),
            )
            onValueChangeState.value(newText)
        }
    }

    Column {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = textState,
            onValueChange = { newValue ->
                if (newValue.text.contains("+")) {
                    if (newValue.text.length > 15) {
                        coroutineScope.launch {
                            shouldShowSupportingText = true
                            delay(2000L)
                            shouldShowSupportingText = false
                        }
                    } else {
                        textState = newValue
                        onValueChange(newValue.text)
                    }
                }
            },
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            color = surfaceHighest,
                            shape = RoundedCornerShape(16.dp),
                        ).padding(
                            vertical = 12.dp,
                            horizontal = 20.dp,
                        ),
                ) {
                    if ((isFocused && value.isEmpty()) || (!isFocused && value.isEmpty())) {
                        if (placeholder.isNotEmpty()) {
                            Text(
                                text = placeholder,
                                style = body_2_regular,
                                color = textSecondary,
                            )
                        }
                    }
                    innerTextField()
                }
            },
            textStyle = body_2_regular.copy(
                color = textPrimary,
            ),
            keyboardOptions = if (isPhoneNumber) KeyboardOptions(keyboardType = KeyboardType.Phone) else KeyboardOptions(),
            maxLines = 1,
            singleLine = true,
            cursorBrush = SolidColor(primary),
        )

        AnimatedVisibility(visible = shouldShowSupportingText) {
            Text(
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 4.dp,
                    ),
                text = supportingText,
                style = label_2_semiBold.copy(
                    fontWeight = FontWeight.Normal,
                ),
                color = textSecondary,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DefaultLazyPizzaTextFieldPreview() {
    DefaultLazyPizzaTextField(
        value = "",
        onValueChange = {},
        placeholder = "+1000 00 0000",
    )
}
