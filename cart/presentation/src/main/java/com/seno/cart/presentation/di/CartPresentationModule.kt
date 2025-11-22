package com.seno.cart.presentation.di

import com.seno.cart.presentation.cart.CartViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cartPresentationModule =
    module {
        viewModelOf(::CartViewModel)
    }
