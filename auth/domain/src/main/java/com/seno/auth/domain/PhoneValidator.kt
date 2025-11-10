package com.seno.auth.domain

interface PhoneValidator {
    fun isValid(phoneNumber: String): Boolean
}
