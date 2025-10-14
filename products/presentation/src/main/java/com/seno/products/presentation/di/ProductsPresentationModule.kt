package com.seno.products.presentation.di

import com.seno.products.presentation.allproducts.AllProductsViewModel
import com.seno.products.presentation.detail.ProductDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val productsPresentationModule =
    module {
        viewModelOf(::AllProductsViewModel)
        viewModelOf(::ProductDetailViewModel)
    }
