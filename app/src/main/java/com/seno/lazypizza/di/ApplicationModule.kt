package com.seno.lazypizza.di

import com.seno.lazypizza.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val applicationModule =
    module {
        viewModelOf(::MainViewModel)
    }
