package com.seno.lazypizza.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.seno.auth.presentation.login.LoginRoot
import com.seno.cart.presentation.cart.CartRoot
import com.seno.cart.presentation.checkout.OrderCheckoutRoot
import com.seno.core.presentation.components.LazyPizzaDefaultScreen
import com.seno.core.presentation.components.bar.NavigationBarItems
import com.seno.core.presentation.model.NavigationMenu
import com.seno.core.presentation.theme.outline
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.core.presentation.utils.ObserveAsEvents
import com.seno.core.presentation.utils.SnackbarController
import com.seno.history.presentation.HistoryRoot
import com.seno.lazypizza.MainState
import com.seno.lazypizza.util.getSelectedMenu
import com.seno.products.presentation.allproducts.AllProductsRoot
import com.seno.products.presentation.detail.ProductDetailRoot
import kotlinx.coroutines.launch

@Composable
fun NavigationRoot(
    state: MainState,
    navHostController: NavHostController,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

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

    LazyPizzaDefaultScreen(
        bottomBar = {
            if (!deviceType.isTablet() && shouldShowNavigationItem(currentRoute)) {
                NavigationBarItems(
                    isTablet = false,
                    selectedMenu = currentRoute.getSelectedMenu(),
                    onNavigationMenuClick = { menu ->
                        onNavigationMenuClick(navHostController, currentRoute, menu)
                    },
                    badgeCounts = mapOf(
                        NavigationMenu.CART to state.totalCartItem,
                        NavigationMenu.HISTORY to 0,
                    ),
                )
            }
        },
        snackbarHostState = snackbarHostState,
    ) {
        if (deviceType.isTablet() && shouldShowNavigationItem(currentRoute)) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                NavigationBarItems(
                    isTablet = true,
                    selectedMenu = currentRoute.getSelectedMenu(),
                    onNavigationMenuClick = { menu ->
                        onNavigationMenuClick(navHostController, currentRoute, menu)
                    },
                )

                VerticalDivider(modifier = Modifier.background(color = outline))

                Box(
                    Modifier
                        .weight(1f),
                ) {
                    NavHost(
                        navController = navHostController,
                        startDestination = Screen.Menu,
                    ) {
                        mainGraph(navHostController)
                        cartGraph(navHostController)
                        historyGraph(navHostController)
                        authGraph(navHostController)
                    }
                }
            }
        } else {
            NavHost(
                navController = navHostController,
                startDestination = Screen.Menu,
            ) {
                mainGraph(navHostController)
                cartGraph(navHostController)
                historyGraph(navHostController)
                authGraph(navHostController)
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
                onNavigateToAuthenticationScreen = {
                    navHostController.navigate(route = Screen.Authentication.LoginScreen)
                },
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
            onBackClick = {
                navHostController.popBackStack()
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
                onNavigateToCheckout = {
                    navHostController.navigate(Screen.Cart.OrderCheckoutScreen)
                },
            )
        }
        composable<Screen.Cart.OrderCheckoutScreen> {
            OrderCheckoutRoot(
                navigateBack = {
                    navHostController.popBackStack()
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
            HistoryRoot(
                onSignInClick = { navHostController.navigate(Screen.Authentication.LoginScreen) },
                onGotoMenuClick = {
                    navHostController.navigate(Screen.Menu) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

private fun NavGraphBuilder.authGraph(navHostController: NavHostController) {
    navigation<Screen.Authentication>(
        startDestination = Screen.Authentication.LoginScreen,
    ) {
        composable<Screen.Authentication.LoginScreen> {
            LoginRoot(
                onContinueWithoutSignIn = {
                    navHostController.navigate(Screen.Menu) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onConfirmButtonClick = {
                    navHostController.navigate(Screen.Menu) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

private fun shouldShowNavigationItem(currentRoute: NavDestination?): Boolean {
    val isProductDetail = currentRoute?.hasRoute<Screen.Menu.ProductDetail>() == true
    val isLogin = currentRoute?.hasRoute<Screen.Authentication.LoginScreen>() == true

    return !(isProductDetail || isLogin)
}

private fun onNavigationMenuClick(
    navHostController: NavHostController,
    currentRoute: NavDestination?,
    menu: NavigationMenu,
) {
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
}
