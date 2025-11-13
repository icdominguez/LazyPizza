package com.seno.auth.data.network.send_code

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendCodeRequest(
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("code_length")
    val codeLength: Int = 6,
    val ttl: Int = 60
)
