package com.seno.cart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.seno.core.domain.product.ProductType
import com.seno.core.presentation.R
import com.seno.core.presentation.model.CartItemUI
import com.seno.core.presentation.theme.body_1_regular
import com.seno.core.presentation.theme.outline50
import com.seno.core.presentation.theme.primary
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_semiBold
import com.seno.core.presentation.utils.formatToPrice

@Composable
internal fun CartToppingCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onClick: () -> Unit,
    cartItem: CartItemUI,
) {
    Column(
        modifier = modifier
            .padding(start = 8.dp)
            .width(160.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(start = 1.dp, top = 1.dp, bottom = 8.dp, end = 1.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 0.dp,
                        topEnd = 12.dp,
                        bottomEnd = 0.dp
                    )
                ),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = cartItem.name,
            style = body_1_regular.copy(color = textSecondary),
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$${cartItem.price.formatToPrice()}",
                style = title_1_semiBold.copy(
                    color = textPrimary
                )
            )
            IconButton(
                onClick = onClick,
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
                    contentDescription = "Add to cart",
                    tint = primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun CartToppingCardPreview() {
    CartToppingCard(
        modifier = Modifier,
        imageUrl = "",
        onClick = {},
        cartItem = CartItemUI(
            reference = "",
            image = "",
            name = "BBQ Sauce",
            price = 0.59,
            type = ProductType.EXTRA_TOPPING,
        )
    )
}