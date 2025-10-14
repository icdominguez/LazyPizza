package com.seno.products.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.seno.products.data.repository.FirestoreProductRepository
import com.seno.products.domain.repository.ProductsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productsDataModule =
    module {
        single {
            FirebaseFirestore.getInstance()
        }

        singleOf(::FirestoreProductRepository) bind ProductsRepository::class
    }
