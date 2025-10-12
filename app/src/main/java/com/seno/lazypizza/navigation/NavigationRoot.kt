package com.seno.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.seno.products.presentation.detail.ProductDetailRoot
import com.seno.products.presentation.allproducts.AllProductsRoot

@Composable
fun NavigationRoot(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.AllProducts
    ) {
        mainGraph(navHostController)
    }
}

private fun NavGraphBuilder.mainGraph(navHostController: NavHostController) {
    composable<Screen.AllProducts> {
        AllProductsRoot(
            onNavigateToProductDetail = { pizzaName ->
                navHostController.navigate(route = Screen.ProductDetail(pizzaName))
            }
        )
    }

    composable<Screen.ProductDetail> {
        ProductDetailRoot(
            onBackClicked = {
                navHostController.navigateUp()
            }
        )
    }
}