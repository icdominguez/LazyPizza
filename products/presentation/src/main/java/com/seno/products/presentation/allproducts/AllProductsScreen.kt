package com.seno.products.presentation.allproducts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.domain.product.ProductType
import com.seno.core.presentation.components.card.PizzaCard
import com.seno.core.presentation.components.card.ProductCard
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.background
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.core.presentation.utils.replaceUnderscores
import com.seno.products.presentation.allproducts.component.AllProductsHeader
import com.seno.products.presentation.allproducts.component.AllProductsTopBar
import com.seno.products.presentation.allproducts.component.LogoutConfirmationDialog
import kotlinx.coroutines.launch

@Composable
fun AllProductsScreen(
    modifier: Modifier = Modifier,
    state: AllProductsState = AllProductsState(),
    onAction: (AllProductsAction) -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    if (state.showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismissRequest = {
                onAction(AllProductsAction.DismissLogoutDialog)
            },
            onConfirmation = {
                onAction(AllProductsAction.ConfirmLogout)
            },
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        AllProductsTopBar(
            isLoggedIn = state.isLoggedIn,
            onLoginClick = {
                onAction(AllProductsAction.OnNavigateToAuthenticationScreen)
            },
            onLogoutClick = {
                onAction(AllProductsAction.ShowLogoutDialog)
            },
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
        ) {
            if (deviceType != DeviceConfiguration.MOBILE_LANDSCAPE) {
                AllProductsHeader(
                    searchQuery = state.searchQuery,
                    onQueryChange = {
                        onAction(AllProductsAction.OnQueryChange(it))
                    },
                    onChipClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                state.headerIndexMap[it] ?: 0,
                            )
                        }
                    },
                )
            }

            if (state.products.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    contentPadding =
                        PaddingValues(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding(),
                        ),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (deviceType == DeviceConfiguration.MOBILE_LANDSCAPE) {
                        item {
                            AllProductsHeader(
                                searchQuery = state.searchQuery,
                                onQueryChange = {
                                    onAction(AllProductsAction.OnQueryChange(it))
                                },
                                onChipClick = {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(
                                            state.headerIndexMap[it] ?: 0,
                                        )
                                    }
                                },
                            )
                        }
                    }
                    state.productsFiltered.forEach { (type, products) ->
                        stickyHeader {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(color = background)
                                        .padding(vertical = 8.dp),
                            ) {
                                Text(
                                    text = type.name.replaceUnderscores(),
                                    style =
                                        label_2_semiBold.copy(
                                            color = textSecondary,
                                        ),
                                )
                            }
                        }

                        val chunkedProducts = products.chunked(if (deviceType.isTablet()) 2 else 1)
                        items(chunkedProducts.size) { rowIndex ->
                            val rowItems = chunkedProducts[rowIndex]

                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                rowItems.forEach { product ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        when (product.type) {
                                            ProductType.PIZZA -> {
                                                PizzaCard(
                                                    imageUrl = product.image,
                                                    pizzaName = product.name,
                                                    pizzaDescription =
                                                        product.ingredients.joinToString(
                                                            ", ",
                                                        ),
                                                    pizzaPrice = product.price,
                                                    onPizzaClick = {
                                                        onAction(
                                                            AllProductsAction.OnProductClicked(
                                                                pizzaName = product.name,
                                                            ),
                                                        )
                                                    },
                                                )
                                            }

                                            else -> {
                                                ProductCard(
                                                    imageUrl = product.image,
                                                    productName = product.name,
                                                    productPrice = product.price,
                                                    quantity = product.quantity,
                                                    onQuantityChange = { newQuantity ->
                                                        when {
                                                            newQuantity == 0 -> {
                                                                onAction(
                                                                    AllProductsAction.OnProductMinus(
                                                                        product,
                                                                    ),
                                                                )
                                                            }

                                                            newQuantity > product.quantity -> {
                                                                onAction(
                                                                    AllProductsAction.OnProductPlus(
                                                                        product,
                                                                    ),
                                                                )
                                                            }

                                                            newQuantity < product.quantity -> {
                                                                onAction(
                                                                    AllProductsAction.OnProductMinus(
                                                                        product,
                                                                    ),
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onDeleteClick = {
                                                        onAction(
                                                            AllProductsAction.OnProductDelete(
                                                                product,
                                                            ),
                                                        )
                                                    },
                                                )
                                            }
                                        }
                                    }
                                }

                                if (deviceType.isTablet() && rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllProductsScreenPreview() {
    LazyPizzaTheme {
        AllProductsScreen()
    }
}
