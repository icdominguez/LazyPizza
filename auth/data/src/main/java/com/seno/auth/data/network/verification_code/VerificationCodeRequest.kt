package com.seno.auth.data.network.verification_code

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationCodeRequest(
    @SerialName("request_id")
    val requestId: String,
    val code: String
)
