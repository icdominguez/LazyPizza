package com.seno.auth.data.di

import com.seno.auth.data.PhoneValidatorImpl
import com.seno.auth.domain.PhoneValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::PhoneValidatorImpl) bind PhoneValidator::class
}
