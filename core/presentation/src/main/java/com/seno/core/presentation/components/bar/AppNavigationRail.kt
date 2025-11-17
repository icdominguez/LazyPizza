package com.seno.core.presentation.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.model.NavigationMenu
import com.seno.core.presentation.theme.textOnPrimary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_4

@Composable
fun AppNavigationRail(
    selectedMenu: NavigationMenu?,
    badgeCounts: Map<NavigationMenu, Int>,
    onNavigationMenuClick: (NavigationMenu) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.Red),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavigationMenu.entries.forEach { screen ->
                val isSelected = selectedMenu == screen
                val itemCount = badgeCounts[screen] ?: 0

                NavigationRailItem(
                    colors = NavigationRailItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = textPrimary,
                        unselectedIconColor = textSecondary,
                        unselectedTextColor = textSecondary,
                    ),
                    selected = selectedMenu == screen,
                    onClick = { onNavigationMenuClick(screen) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (itemCount > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = textOnPrimary,
                                    ) {
                                        Text(
                                            text = "$itemCount",
                                            style = title_4,
                                        )
                                    }
                                }
                            },
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier =
                                    Modifier
                                        .size(40.dp)
                                        .clickable(
                                            interactionSource = null,
                                            indication = null,
                                        ) {
                                            onNavigationMenuClick(screen)
                                        }.then(
                                            if (isSelected) {
                                                Modifier.background(
                                                    color = MaterialTheme.colorScheme.primary.copy(
                                                        alpha = 0.08f,
                                                    ),
                                                    shape = CircleShape,
                                                )
                                            } else {
                                                Modifier
                                            },
                                        ),
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(screen.icon),
                                    contentDescription = screen.title,
                                )
                            }
                        }
                    },
                    label = {
                        Text(
                            text = screen.title,
                            style = title_4,
                            textAlign = TextAlign.Center,
                        )
                    },
                )
            }
        }
    }
}
