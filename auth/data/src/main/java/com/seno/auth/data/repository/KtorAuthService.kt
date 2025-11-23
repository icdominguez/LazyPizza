package com.seno.auth.data.repository

import com.seno.auth.data.network.post
import com.seno.auth.data.network.send_code.SendCodeError
import com.seno.auth.data.network.send_code.SendCodeRequest
import com.seno.auth.data.network.send_code.SendCodeResponse
import com.seno.auth.data.network.verification_code.VerificationCodeError
import com.seno.auth.data.network.verification_code.VerificationCodeRequest
import com.seno.auth.data.network.verification_code.VerificationCodeResponse
import com.seno.auth.domain.model.RequestId
import com.seno.auth.domain.repository.AuthService
import com.seno.core.domain.DataError
import com.seno.core.domain.EmptyResult
import com.seno.core.domain.Result
import com.seno.core.domain.map
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {
    override suspend fun sendCode(phoneNumber: String): Result<RequestId, DataError.Network> {
        val result = httpClient.post<SendCodeRequest, SendCodeResponse>(
            route = "/sendVerificationMessage",
            body = SendCodeRequest(
                phoneNumber = phoneNumber,
            ),
        )

        return when (result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> result.map {
                it.result?.requestId ?: it.error?.let { error ->
                    return when (error) {
                        SendCodeError.PHONE_NUMBER_INVALID.name -> Result.Error(DataError.Network.PHONE_NUMBER_INVALID)
                        SendCodeError.PHONE_NUMBER_NOT_AVAILABLE.name -> Result.Error(DataError.Network.PHONE_NUMBER_NOT_AVAILABLE)
                        SendCodeError.BALANCE_NOT_ENOUGH.name -> Result.Error(DataError.Network.BALANCE_NOT_ENOUGH)
                        else -> Result.Error(DataError.Network.SERVER_ERROR)
                    }
                } ?: return Result.Error(DataError.Network.SERVER_ERROR)
            }
        }
    }

    override suspend fun verificationCode(
        requestId: RequestId,
        code: String
    ): EmptyResult<DataError.Network> {
        val result = httpClient.post<VerificationCodeRequest, VerificationCodeResponse>(
            route = "/checkVerificationStatus",
            body = VerificationCodeRequest(
                requestId = requestId,
                code = code,
            ),
        )

        return when (result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> result.map {
                it.error?.let { error ->
                    return when (error) {
                        VerificationCodeError.REQUEST_ID_INVALID.name -> Result.Error(DataError.Network.REQUEST_ID_INVALID)
                        else -> Result.Error(DataError.Network.SERVER_ERROR)
                    }
                } ?: it.result?.verificationStatus?.let { verificationStatus ->
                    if (verificationStatus.status == VerificationCodeError.CODE_INVALID.name.lowercase()) {
                        return Result.Error(DataError.Network.VERIFICATION_CODE_INVALID)
                    }
                }
            }
        }
    }
}
