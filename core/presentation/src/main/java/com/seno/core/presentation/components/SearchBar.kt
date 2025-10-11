package com.seno.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.R
import com.seno.core.presentation.theme.body_1_regular
import com.seno.core.presentation.theme.textSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizableSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    var hasFocus by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = Modifier.onFocusChanged { focusState ->
            hasFocus = focusState.isFocused
        },
        value = query,
        onValueChange = { onQueryChange(it) },
        textStyle = body_1_regular,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        shape = RoundedCornerShape(16.dp),
                        elevation = 2.dp
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(
                        vertical = 14.dp,
                        horizontal = 16.dp,
                    ),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.search_ic),
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    if(query.isEmpty() && !hasFocus) {
                        Text(
                            text = "Search for delicious food...",
                            style = body_1_regular,
                            color = textSecondary
                        )
                    }
                    innerTextField()
                }
            }
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    CustomizableSearchBar(
        query = "",
        onQueryChange = {},
    )
}