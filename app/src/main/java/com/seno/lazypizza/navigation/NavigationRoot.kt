package com.seno.lazypizza.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.seno.core.presentation.components.LazyPizzaDefaultScreen
import com.seno.core.presentation.components.bar.LazyPizzaMenuBar
import com.seno.core.presentation.model.NavigationMenu
import com.seno.lazypizza.util.getSelectedMenu
import com.seno.products.presentation.allproducts.AllProductsRoot
import com.seno.products.presentation.allproducts.component.AllProductsTopBar
import com.seno.products.presentation.detail.ProductDetailRoot
import com.seno.products.presentation.detail.component.ProductDetailTopBar

@Composable
fun NavigationRoot(
    navHostController: NavHostController,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    LazyPizzaDefaultScreen(
        topAppBar = {
            when {
                currentRoute?.hasRoute<Screen.Menu.ProductDetail>() == true -> {
                    ProductDetailTopBar(
                        onBackClicked = {
                            navHostController.navigateUp()
                        }
                    )
                }

                currentRoute?.hasRoute<Screen.Menu.AllProducts>() == true -> {
                    AllProductsTopBar()
                }
            }
        },
    ) {
        val state = rememberNavigationSuiteScaffoldState()

        LaunchedEffect(currentRoute) {
            if (currentRoute?.hasRoute<Screen.Menu.ProductDetail>() == true) {
                state.hide()
            } else state.show()
        }

        LazyPizzaMenuBar(
            state = state,
            selectedMenu = currentRoute.getSelectedMenu(),
            onNavigationMenuClick = { menu ->
                val currentParentRoute = currentRoute?.parent?.route

                when (menu) {
                    NavigationMenu.HOME -> {
                        navHostController.navigate(Screen.Menu) {
                            launchSingleTop = true
                            restoreState = true
                            currentParentRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                    inclusive = true
                                }
                            }
                        }
                    }

                    NavigationMenu.CART -> {
                        navHostController.navigate(Screen.Cart) {
                            launchSingleTop = true
                            restoreState = true
                            currentParentRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                    inclusive = true
                                }
                            }
                        }
                    }

                    NavigationMenu.HISTORY -> {
                        navHostController.navigate(Screen.History) {
                            launchSingleTop = true
                            restoreState = true
                            currentParentRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Screen.Menu
            ) {
                mainGraph(navHostController)
                cartGraph(navHostController)
                historyGraph(navHostController)
            }
        }
    }
}

private fun NavGraphBuilder.mainGraph(navHostController: NavHostController) {
    navigation<Screen.Menu>(
        startDestination = Screen.Menu.AllProducts
    ) {
        composable<Screen.Menu.AllProducts> {
            AllProductsRoot(
                onNavigateToProductDetail = { pizzaName ->
                    navHostController.navigate(route = Screen.Menu.ProductDetail(pizzaName))
                })
        }
    }

    composable<Screen.Menu.ProductDetail> {
        ProductDetailRoot()
    }
}

private fun NavGraphBuilder.cartGraph(navHostController: NavHostController) {
    navigation<Screen.Cart>(
        startDestination = Screen.Cart.CartScreen
    ) {
        composable<Screen.Cart.CartScreen> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("Cart")
            }
        }
    }
}

private fun NavGraphBuilder.historyGraph(navHostController: NavHostController) {
    navigation<Screen.History>(
        startDestination = Screen.History.HistoryScreen
    ) {
        composable<Screen.History.HistoryScreen> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("History")
            }
        }
    }
}