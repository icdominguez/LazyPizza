package com.seno.auth.domain.repository

import com.seno.auth.domain.model.RequestId
import com.seno.core.domain.DataError
import com.seno.core.domain.EmptyResult
import com.seno.core.domain.Result

interface AuthService {
    /**
     * Sends a verification code to the provided phone number.
     *
     * @param phoneNumber The phone number to send the verification code to.
     * @return A [Result] containing the [RequestId] on success, which is needed for the `verificationCode` step.
     * On failure, it returns a [DataError.Network] subtype. This error should be handled in the ViewModel
     * to show a user-friendly message, for example by using `DataError.asUiText()`.
     */
    suspend fun sendCode(phoneNumber: String): Result<RequestId, DataError.Network>

    /**
     * Verifies the phone number using the provided code and request ID.
     *
     * @param requestId The ID received from the [sendCode] function.
     * @param code The verification code entered by the user.
     * @return An [EmptyResult] which is [Result.Success] if the code is valid,
     * or [Result.Error] with a [DataError.Network] value if it fails.
     */
    suspend fun verificationCode(
        requestId: RequestId,
        code: String,
    ): EmptyResult<DataError.Network>
}
