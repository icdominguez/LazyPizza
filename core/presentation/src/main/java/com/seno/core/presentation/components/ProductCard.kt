package com.seno.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.seno.core.presentation.R
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_1_medium
import com.seno.core.presentation.theme.body_4_regular
import com.seno.core.presentation.theme.outline50
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_semiBold
import com.seno.core.presentation.utils.formatToPrice
import java.util.Locale

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    productName: String,
    productPrice: Double
) {
    var quantity by remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Pizza",
            modifier = Modifier
                .size(108.dp)
                .padding(start = 1.dp, top = 1.dp, bottom = 1.dp, end = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                ),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = productName,
                    style = body_1_medium.copy(color = textPrimary)
                )

                if (quantity > 0) {
                    IconButton(
                        onClick = {
                            quantity = 0
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .border(
                                width = 1.dp,
                                color = outline50,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(4.dp)
                            .size(22.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.trash_ic),
                            contentDescription = "Remove",
                            tint = Color.Unspecified
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (quantity == 0) {
                    Text(
                        text = "$${productPrice.formatToPrice()}",
                        style = title_1_semiBold.copy(color = textPrimary)
                    )

                    LazyPizzaSecondaryButton(
                        buttonText = "Add to Cart",
                        onClick = { quantity = 1 }
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (quantity > 0) quantity-- },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .border(
                                    width = 1.dp,
                                    color = outline50,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(4.dp)
                                .size(22.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.minus_ic),
                                contentDescription = "Decrease",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        AnimatedContent(
                            quantity,
                            transitionSpec = {
                                slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                            }
                        ) {
                            Text(
                                text = it.toString(),
                                style = MaterialTheme.typography.titleLarge,
                            )

                        }

                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .border(
                                    width = 1.dp,
                                    color = outline50,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(4.dp)
                                .size(22.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.plus_ic),
                                contentDescription = "Increase",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "$${
                                String.format(
                                    Locale.ROOT,
                                    "%.2f",
                                    productPrice * quantity
                                )
                            }",
                            style = title_1_semiBold.copy(color = textPrimary)
                        )
                        Text(
                            text = "$quantity x $${
                                String.format(
                                    Locale.ROOT,
                                    "%.2f",
                                    productPrice
                                )
                            }",
                            style = body_4_regular.copy(textSecondary)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    LazyPizzaTheme {
        ProductCard(
            imageUrl = "",
            productName = "Mineral Water",
            productPrice = 1.49
        )
    }
}
