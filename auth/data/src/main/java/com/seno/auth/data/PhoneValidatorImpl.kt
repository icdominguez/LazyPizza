package com.seno.auth.data

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.seno.auth.domain.PhoneValidator
import timber.log.Timber

class PhoneValidatorImpl : PhoneValidator {
    private val phoneUtil = PhoneNumberUtil.getInstance()

    override fun isValid(phoneNumber: String): Boolean =
        try {
            val number = phoneUtil.parse(phoneNumber, null)
            phoneUtil.isValidNumber(number)
        } catch (e: NumberParseException) {
            Timber.tag("PhoneNumberValidation").e("Error validating $phoneNumber: ${e.message}")
            false
        }
}
