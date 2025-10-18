package com.seno.products.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.seno.core.presentation.R
import com.seno.core.presentation.components.LazyPizzaPrimaryButton
import com.seno.core.presentation.components.ToppingCard
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.background
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.label_2_semiBold
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_semiBold
import com.seno.core.presentation.utils.formatToPrice
import com.seno.core.domain.product.Product
import com.seno.products.presentation.detail.ProductDetailAction
import com.seno.products.presentation.detail.ProductDetailState

@Composable
internal fun ProductDetailMobile(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var buttonHeight by remember {
        mutableStateOf(0.dp)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        color = background,
                        size = Size(
                            width = 100.dp.toPx(),
                            height = 50.dp.toPx()
                        )
                    )
                }
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
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

            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                text = stringResource(com.seno.products.presentation.R.string.add_extra_toppings),
                style = label_2_semiBold,
                color = textSecondary
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(top = 8.dp, bottom = buttonHeight / 2),
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(125.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                                    MaterialTheme.colorScheme.surface
                                ),
                            )
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    LazyPizzaPrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned {
                                buttonHeight = it.size.height.dp
                            },
                        onClick = { onAction(ProductDetailAction.OnAddToCartButtonClick) },
                        buttonText = "Add to Cart for $${state.selectedPizza?.price?.formatToPrice()}"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailMobilePreview() {
    LazyPizzaTheme {
        ProductDetailMobile(
            state = ProductDetailState(
                selectedPizza = Product.Pizza(
                    id = "",
                    name = "Margherita",
                    price = 8.00,
                    image = "",
                    ingredients = listOf(
                        "Tomato sauce",
                        "Mozzarella",
                        "Basil"
                    )
                )
            ),
            onAction = {}
        )
    }
}