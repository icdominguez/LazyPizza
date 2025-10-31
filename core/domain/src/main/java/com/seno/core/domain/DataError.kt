package com.seno.core.domain

sealed interface DataError : Error {
    enum class Network : DataError

    enum class Local : DataError {
        DISK_FULL,
    }
}

interface Error
