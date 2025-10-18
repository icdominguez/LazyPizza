package com.seno.products.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.seno.core.presentation.R
import com.seno.core.presentation.components.button.LazyPizzaPrimaryButton
import com.seno.core.presentation.components.card.ToppingCard
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_semiBold
import com.seno.core.presentation.utils.DeviceConfiguration
import com.seno.core.presentation.utils.applyIf
import com.seno.core.presentation.utils.formatToPrice
import com.seno.products.presentation.detail.ProductDetailAction
import com.seno.products.presentation.detail.ProductDetailState

@Composable
internal fun ProductDetailTablet(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 16.dp
                        )
                    )
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    ),
                contentAlignment = Alignment.Center
            ) {
                state.selectedPizza?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(state.selectedPizza.image)
                            .crossfade(true)
                            .build(),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        contentDescription = "Pizza",
                        modifier = Modifier
                            .wrapContentSize()
                            .size(240.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text(
                text = state.selectedPizza?.name.orEmpty(),
                style = title_1_semiBold,
                color = textPrimary
            )

            Text(
                text = state.selectedPizza?.ingredients.orEmpty().joinToString(", "),
                style = body_3_regular,
                color = textSecondary,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .applyIf(deviceType == DeviceConfiguration.TABLET_LANDSCAPE) {
                    padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    )
                },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                text = stringResource(com.seno.products.presentation.R.string.add_extra_toppings),
                style = label_2_semiBold,
                color = textSecondary
            )

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyIf(deviceType == DeviceConfiguration.TABLET_LANDSCAPE) {
                        weight(1f)
                    },
                contentPadding = PaddingValues(
                    top = 8.dp,
                ),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.listExtraToppings,
                    key = { it.name }
                ) { topping ->
                    ToppingCard(
                        imageUrl = topping.image,
                        toppingName = topping.name,
                        toppingPrice = topping.price,
                        quantity = topping.quantity,
                        onQuantityPlus = {
                            onAction(ProductDetailAction.OnToppingPlus(topping))
                        },
                        onQuantityMinus = {
                            onAction(ProductDetailAction.OnToppingMinus(topping))
                        }
                    )
                }
            }

            LazyPizzaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onAction(ProductDetailAction.OnAddToCartButtonClick) },
                buttonText = "Add to Cart for $${state.selectedPizza?.price?.formatToPrice()}"
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_TABLET
)
@Composable
private fun ProductDetailTabletPreview() {
    LazyPizzaTheme {
        ProductDetailTablet(
            state = ProductDetailState(),
            onAction = {}
        )
    }
}