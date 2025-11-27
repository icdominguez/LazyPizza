package com.seno.cart.presentation.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.body_2_regular
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.surfaceHighest
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary

@Composable
fun CommentsUI(
    value: String,
    onValueChange: (String) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(
        modifier = Modifier
            .padding(top = 16.dp),
    ) {
        Text(
            text = "COMMENTS",
            style = label_2_semiBold,
            color = textSecondary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            modifier = Modifier
                .heightIn(min = 92.dp)
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = body_2_regular.copy(
                color = textPrimary,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(
                            color = surfaceHighest,
                            shape = RoundedCornerShape(16.dp),
                        ).padding(
                            vertical = 13.dp,
                            horizontal = 20.dp,
                        ),
                ) {
                    if ((isFocused && value.isEmpty()) || (!isFocused && value.isEmpty())) {
                        Text(
                            text = "Add Comment",
                            style = body_2_regular,
                            color = textSecondary,
                        )
                    }
                    innerTextField()
                }
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentsUIPreview() {
    CommentsUI(value = "")
}
