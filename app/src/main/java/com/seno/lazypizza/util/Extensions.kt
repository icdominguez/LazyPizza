package com.seno.lazypizza.util

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.seno.core.presentation.model.NavigationMenu
import com.seno.lazypizza.navigation.Screen

fun NavDestination?.getSelectedMenu(): NavigationMenu? {
    return when {
        this?.hierarchy?.any { it.hasRoute<Screen.Menu.AllProducts>() } == true -> NavigationMenu.HOME
        this?.hierarchy?.any { it.hasRoute<Screen.Cart>() } == true -> NavigationMenu.CART
        this?.hierarchy?.any { it.hasRoute<Screen.History>() } == true -> NavigationMenu.HISTORY
        else -> null
    }
}