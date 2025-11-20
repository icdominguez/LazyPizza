package com.seno.cart.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.cart.data.repository.FirestoreCartRepository
import com.seno.cart.data.repository.FirestoreCheckoutRepository
import com.seno.cart.domain.CartRepository
import com.seno.cart.domain.CheckoutRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cartDataModule =
    module {
        single {
            FirebaseFirestore.getInstance()
        }

        singleOf(::FirestoreCartRepository) bind CartRepository::class
        singleOf(::FirestoreCheckoutRepository) bind CheckoutRepository::class
    }
