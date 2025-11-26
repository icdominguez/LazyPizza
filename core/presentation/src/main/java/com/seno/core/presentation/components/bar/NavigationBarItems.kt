package com.seno.core.presentation.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.model.NavigationMenu
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.background

@Composable
fun NavigationBarItems(
    selectedMenu: NavigationMenu?,
    onNavigationMenuClick: (NavigationMenu) -> Unit,
    isTablet: Boolean = false,
    badgeCounts: Map<NavigationMenu, Int> = emptyMap(),
) {
    if (isTablet) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    color = background,
                ).padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavigationMenu.entries.map { navigationMenu ->
                NavigationBarItem(
                    navigationMenu = navigationMenu,
                    onNavigationMenuClick = onNavigationMenuClick,
                    isSelected = selectedMenu == navigationMenu,
                    badgeCount = badgeCounts[navigationMenu] ?: 0,
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                ) {
                    radius = 16f
                    color = Color(0xFF03131F)
                    spread = 4f
                    alpha = 0.25f
                }.clip(
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                ).background(
                    color = background,
                ).padding(
                    top = 10.dp,
                    bottom = 30.dp,
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            NavigationMenu.entries.map { navigationMenu ->
                NavigationBarItem(
                    navigationMenu = navigationMenu,
                    onNavigationMenuClick = onNavigationMenuClick,
                    isSelected = selectedMenu == navigationMenu,
                    badgeCount = badgeCounts[navigationMenu] ?: 0,
                )
            }
        }
    }
}

@Preview
@Composable
private fun NavigationBarItemsTabletPreview() {
    LazyPizzaTheme {
        NavigationBarItems(
            isTablet = true,
            selectedMenu = NavigationMenu.HOME,
            onNavigationMenuClick = {},
        )
    }
}

@Preview
@Composable
private fun NavigationBarItemsMobilePreview() {
    LazyPizzaTheme {
        NavigationBarItems(
            isTablet = false,
            selectedMenu = NavigationMenu.HOME,
            onNavigationMenuClick = {},
        )
    }
}
