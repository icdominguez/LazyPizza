package com.seno.core.presentation.components.card

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.seno.core.presentation.R
import com.seno.core.presentation.theme.LazyPizzaTheme
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.outline50
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.primary8
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_2
import com.seno.core.presentation.utils.formatToPrice

@Composable
fun ToppingCard(
    imageUrl: String,
    toppingName: String,
    toppingPrice: Double,
    quantity: Int,
    onQuantityPlus: () -> Unit,
    onQuantityMinus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { if (quantity == 0) onQuantityPlus() }
            .border(
                width = 1.dp,
                color = if (quantity > 0) primary else outline50,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = primary8.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(100)
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Pizza",
                modifier = Modifier
                    .size(56.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = toppingName,
            style = body_3_regular.copy(color = textSecondary)
        )

        Box(
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (quantity == 0) {
                Text(
                    text = "$${toppingPrice.formatToPrice()}",
                    style = title_2.copy(color = textPrimary)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { onQuantityMinus() },
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
                        onClick = { onQuantityPlus() },
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToppingCardPreview() {
    LazyPizzaTheme {
        ToppingCard(
            imageUrl = "",
            toppingName = "Sauce",
            toppingPrice = 1.00,
            quantity = 1,
            onQuantityPlus = {},
            onQuantityMinus = {}
        )
    }
}