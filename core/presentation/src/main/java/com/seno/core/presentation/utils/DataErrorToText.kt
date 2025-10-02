package com.seno.core.presentation.utils

import com.seno.core.domain.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        else -> UiText.DynamicString("Unknown error")
    }
}