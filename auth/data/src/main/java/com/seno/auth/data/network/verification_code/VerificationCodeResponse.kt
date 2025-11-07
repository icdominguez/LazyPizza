package com.seno.auth.data.network.verification_code

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationCodeResponse(
    @SerialName("ok")
    val ok: Boolean,
    @SerialName("result")
    val result: Result? = null,
    val error: String? = null
)

@Serializable
data class Result(
    @SerialName("delivery_status")
    val deliveryStatus: DeliveryStatus,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("request_cost")
    val requestCost: Int,
    @SerialName("request_id")
    val requestId: String,
    @SerialName("verification_status")
    val verificationStatus: VerificationStatus
)

@Serializable
data class DeliveryStatus(
    @SerialName("status")
    val status: String,
    @SerialName("updated_at")
    val updatedAt: Int
)

@Serializable
data class VerificationStatus(
    @SerialName("code_entered")
    val codeEntered: String,
    @SerialName("status")
    val status: String,
    @SerialName("updated_at")
    val updatedAt: Int
)
