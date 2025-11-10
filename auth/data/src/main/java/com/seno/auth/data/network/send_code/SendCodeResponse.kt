package com.seno.auth.data.network.send_code

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendCodeResponse(
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
    @SerialName("remaining_balance")
    val remainingBalance: Int,
    @SerialName("request_cost")
    val requestCost: Int,
    @SerialName("request_id")
    val requestId: String
)

@Serializable
data class DeliveryStatus(
    @SerialName("status")
    val status: String,
    @SerialName("updated_at")
    val updatedAt: Int
)
