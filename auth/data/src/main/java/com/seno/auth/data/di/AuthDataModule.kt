package com.seno.auth.data.di

import com.seno.auth.data.PhoneValidatorImpl
import com.seno.auth.data.network.HttpClientFactory
import com.seno.auth.data.repository.KtorAuthService
import com.seno.auth.domain.PhoneValidator
import com.seno.auth.domain.repository.AuthService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::PhoneValidatorImpl) bind PhoneValidator::class
    single { HttpClientFactory().build() }
    singleOf(::KtorAuthService) bind AuthService::class
}
