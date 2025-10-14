package com.seno.core.presentation.utils

import com.seno.core.domain.DataError

fun DataError.asUiText(): UiText =
    when (this) {
        else -> UiText.DynamicString("Unknown error")
    }
