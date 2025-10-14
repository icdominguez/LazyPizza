@file:OptIn(ExperimentalMaterial3Api::class)

package com.seno.products.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.textSecondary8

@Composable
fun ProductDetailTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        title = {},
        navigationIcon = {
            IconButton(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .size(32.dp)
                        .background(
                            color = textSecondary8.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(100),
                        ),
                onClick = { onBackClick() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "back",
                    tint = textSecondary,
                )
            }
        },
    )
}
