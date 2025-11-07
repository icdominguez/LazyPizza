package com.seno.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.primaryGradient

fun Modifier.circleGradientShadow(enabled: Boolean) =
    if (enabled) {
        this
            .background(primaryGradient, CircleShape)
            .dropShadow(RoundedCornerShape(20.dp)) {
                radius = 10f
                color = primary
                spread = 6f
                alpha = 0.25f
            }
    } else {
        this
    }
