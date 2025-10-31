package com.seno.products.presentation.allproducts

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.domain.product.ProductType
import com.seno.core.presentation.components.bar.CustomizableSearchBar
import com.seno.core.presentation.components.card.PizzaCard
import com.seno.core.presentation.components.card.ProductCard
import com.seno.core.presentation.components.chip.ProductChip
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.background
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.core.presentation.utils.replaceUnderscores
import com.seno.core.presentation.utils.toCamelCase
import kotlinx.coroutines.launch

@Composable
fun AllProductsScreen(
    state: AllProductsState = AllProductsState(),
    onAction: (AllProductsAction) -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Column(
        modifier =
            Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                ),
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        shape = RoundedCornerShape(16.dp),
                    ),
            contentScale = ContentScale.Crop,
            painter = painterResource(com.seno.products.presentation.R.drawable.banner),
            contentDescription = "Banner",
        )

        Spacer(modifier = Modifier.size(16.dp))

        CustomizableSearchBar(
            query = state.searchQuery,
            onQueryChange = { onAction(AllProductsAction.OnQueryChange(it)) },
        )

        LazyRow(
            modifier =
                Modifier
                    .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            items(
                ProductType.entries,
                key = { it.name },
            ) { productType ->
                if (productType != ProductType.EXTRA_TOPPING) {
                    ProductChip(
                        chipText = productType.name.toCamelCase(),
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(
                                    state.headerIndexMap[productType] ?: 0,
                                )
                            }
                        },
                    )
                }
            }
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
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AllProductsScreenPreview() {
    LazyPizzaTheme {
        AllProductsScreen()
    }
}
