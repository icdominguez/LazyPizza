package com.seno.core.presentation.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.model.NavigationMenu
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_4

@Composable
fun NavigationBarItem(
    navigationMenu: NavigationMenu,
    onNavigationMenuClick: (NavigationMenu) -> Unit = {},
    isSelected: Boolean = false,
    badgeCount: Int = 0
) {
    Column(
        modifier = Modifier
            .defaultMinSize(minWidth = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (isSelected) primary.copy(alpha = 0.08f) else Color.Transparent,
                        shape = CircleShape,
                    ).clickable(
                        onClick = { onNavigationMenuClick(navigationMenu) },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(navigationMenu.icon),
                    contentDescription = navigationMenu.title,
                    tint = if (isSelected) primary else textSecondary,
                )
            }

            if (navigationMenu == NavigationMenu.CART && badgeCount > 0) {
                CustomBadge(
                    modifier = Modifier
                        .offset(x = 8.dp, y = (-6).dp),
                    items = badgeCount,
                )
            }
        }

        Spacer(modifier = Modifier.size(2.dp))

        Text(
            text = navigationMenu.title,
            style = title_4,
            color = if (isSelected) textPrimary else textSecondary,
        )
    }
}

@Preview
@Composable
private fun NavigationBarItemPreview() {
    NavigationBarItem(
        navigationMenu = NavigationMenu.CART,
    )
}

@Preview
@Composable
private fun NavigationBarItemSelectedPreview() {
    NavigationBarItem(
        navigationMenu = NavigationMenu.CART,
        isSelected = true,
    )
}

@Preview
@Composable
private fun NavigationBarItemSelectedBadgePreview() {
    NavigationBarItem(
        navigationMenu = NavigationMenu.CART,
        isSelected = true,
        badgeCount = 9,
    )
}
