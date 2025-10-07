package com.seno.lazypizza

import android.app.Application
import com.seno.products.data.di.productsDataModule
import com.seno.products.presentation.di.productsPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class LazyPizzaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@LazyPizzaApp)
            androidLogger()
            modules(
                productsDataModule,
                productsPresentationModule
            )
        }
    }
}