package com.seno.core.data.di

import com.seno.core.data.datastore.UserDataDataStore
import com.seno.core.data.datastore.dataStore
import com.seno.core.domain.userdata.UserData
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule =
    module {
        single { androidContext().dataStore }

        single<UserData> { UserDataDataStore(get()) }
    }
