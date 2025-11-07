package com.seno.core.presentation.utils

import com.seno.core.domain.DataError
import com.seno.core.presentation.R

// TODO SET The error message from string resource
fun DataError.asUiText(): UiText =
    when (this) {
        DataError.Local.DISK_FULL -> TODO()
        DataError.Local.NOT_FOUND -> TODO()
        DataError.Local.UNKNOWN -> TODO()
        DataError.Network.BAD_REQUEST -> TODO()
        DataError.Network.REQUEST_TIMEOUT -> TODO()
        DataError.Network.UNAUTHORIZED -> TODO()
        DataError.Network.FORBIDDEN -> TODO()
        DataError.Network.NOT_FOUND -> TODO()
        DataError.Network.CONFLICT -> TODO()
        DataError.Network.TOO_MANY_REQUESTS -> TODO()
        DataError.Network.NO_INTERNET -> TODO()
        DataError.Network.PAYLOAD_TOO_LARGE -> TODO()
        DataError.Network.SERVER_ERROR -> TODO()
        DataError.Network.SERVICE_UNAVAILABLE -> TODO()
        DataError.Network.SERIALIZATION -> TODO()
        DataError.Network.PHONE_NUMBER_INVALID -> TODO()
        DataError.Network.BALANCE_NOT_ENOUGH -> TODO()
        DataError.Network.UNKNOWN -> TODO()
        else -> UiText.StringResource(
            R.string.error_unknown,
        )
    }
