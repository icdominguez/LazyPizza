package com.seno.history.data.di

import com.seno.history.data.repository.FirestoreHistoryRepository
import com.seno.history.domain.HistoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val historyDataModule = module {
    singleOf(::FirestoreHistoryRepository) bind HistoryRepository::class
}
