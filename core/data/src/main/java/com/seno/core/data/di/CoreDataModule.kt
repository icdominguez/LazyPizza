package com.seno.core.data.di

import com.seno.core.data.datastore.UserDataDataStore
import com.seno.core.data.datastore.dataStore
import com.seno.core.data.repository.FirestoreCoreRepository
import com.seno.core.domain.repository.CoreRepository
import com.seno.core.domain.userdata.UserData
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single { androidContext().dataStore }

    single<UserData> { UserDataDataStore(get()) }
    singleOf(::FirestoreCoreRepository) bind CoreRepository::class
}
