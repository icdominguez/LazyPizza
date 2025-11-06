package com.seno.lazypizza.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.seno.cart.presentation.CartRoot
import com.seno.cart.presentation.components.CartTopBar
import com.seno.core.presentation.components.LazyPizzaDefaultScreen
import com.seno.core.presentation.components.bar.LazyPizzaMenuBar
import com.seno.core.presentation.model.NavigationMenu
import com.seno.core.presentation.utils.ObserveAsEvents
import com.seno.core.presentation.utils.SnackbarController
import com.seno.history.presentation.HistoryScreenRoot
import com.seno.history.presentation.component.HistoryTopBar
import com.seno.lazypizza.MainAction
import com.seno.lazypizza.MainState
import com.seno.lazypizza.util.getSelectedMenu
import com.seno.products.presentation.allproducts.AllProductsRoot
import com.seno.products.presentation.allproducts.component.AllProductsTopBar
import com.seno.products.presentation.allproducts.component.LogoutConfirmationDialog
import com.seno.products.presentation.detail.ProductDetailRoot
import com.seno.products.presentation.detail.component.ProductDetailTopBar
import kotlinx.coroutines.launch

@Composable
fun NavigationRoot(
    state: MainState,
    onAction: (MainAction) -> Unit = {},
    navHostController: NavHostController,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(
        SnackbarController.events,
        snackbarHostState,
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result =
                snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.name,
                    withDismissAction = event.onDismiss != null,
                    duration = SnackbarDuration.Short,
                )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }
    if (state.showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismissRequest = {
                onAction(MainAction.DismissLogoutDialog)
            },
            onConfirmation = {
                onAction(MainAction.ConfirmLogout)
            }
        )
    }

    LazyPizzaDefaultScreen(
        snackbarHostState = snackbarHostState,
        topAppBar = {
            when {
                currentRoute?.hasRoute<Screen.Menu.ProductDetail>() == true -> {
                    ProductDetailTopBar(
                        onBackClick = {
                            navHostController.navigateUp()
                        },
                    )
                }

                currentRoute?.hasRoute<Screen.Menu.AllProducts>() == true -> {
                    AllProductsTopBar(
                        isLoggedIn = state.isLoggedIn,
                        onLoginClick = {
//                            navHostController.navigate(Screen.Login)
                        },
                        onLogoutClick = {
                            onAction(MainAction.ShowLogoutDialog)
                        },
                    )
                }

                currentRoute?.hasRoute<Screen.History.HistoryScreen>() == true -> {
                    HistoryTopBar()
                }
                currentRoute?.hasRoute<Screen.Cart.CartScreen>() == true -> {
                    CartTopBar()
                }
            }
        },
    ) {
        val suiteScaffoldState = rememberNavigationSuiteScaffoldState()

        LaunchedEffect(currentRoute) {
            if (currentRoute?.hasRoute<Screen.Menu.ProductDetail>() == true) {
                suiteScaffoldState.hide()
            } else {
                suiteScaffoldState.show()
            }
        }

        LazyPizzaMenuBar(
            state = suiteScaffoldState,
            selectedMenu = currentRoute.getSelectedMenu(),
            badgeCounts =
                mapOf(
                    NavigationMenu.CART to state.totalCartItem,
                    NavigationMenu.HISTORY to 0,
                ),
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
                            currentParentRoute?.let {
                                popUpTo(it) {
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
            },
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Screen.Menu,
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
        startDestination = Screen.Menu.AllProducts,
    ) {
        composable<Screen.Menu.AllProducts> {
            AllProductsRoot(
                onNavigateToProductDetail = { pizzaName ->
                    navHostController.navigate(route = Screen.Menu.ProductDetail(pizzaName))
                },
            )
        }
    }

    composable<Screen.Menu.ProductDetail> {
        ProductDetailRoot(
            onAddToCartClick = {
                navHostController.navigateUp()
            },
        )
    }
}

private fun NavGraphBuilder.cartGraph(navHostController: NavHostController) {
    navigation<Screen.Cart>(
        startDestination = Screen.Cart.CartScreen,
    ) {
        composable<Screen.Cart.CartScreen> {
            CartRoot(
                onNavigateToMenu = {
                    navHostController.navigate(Screen.Menu) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

private fun NavGraphBuilder.historyGraph(navHostController: NavHostController) {
    navigation<Screen.History>(
        startDestination = Screen.History.HistoryScreen,
    ) {
        composable<Screen.History.HistoryScreen> {
            HistoryScreenRoot()
        }
    }
}
