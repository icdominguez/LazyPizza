package com.seno.history.presentation.di

import com.seno.history.presentation.HistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val historyPresentationModule =
    module {
        viewModelOf(::HistoryViewModel)
    }
