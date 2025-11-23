package com.seno.core.domain

sealed interface DataError : Error {
    enum class Network : DataError {
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERVICE_UNAVAILABLE,
        SERIALIZATION,
        PHONE_NUMBER_INVALID,
        PHONE_NUMBER_NOT_AVAILABLE,
        REQUEST_ID_INVALID,
        VERIFICATION_CODE_INVALID,
        BALANCE_NOT_ENOUGH,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN
    }
}

interface Error
