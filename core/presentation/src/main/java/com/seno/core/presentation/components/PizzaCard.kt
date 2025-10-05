package com.seno.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.R
import com.seno.core.presentation.theme.body_1_medium
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_1_semiBold

@Composable
fun PizzaCard(
    modifier: Modifier = Modifier,
    painterRes: Painter,
    pizzaName: String,
    pizzaDescription: String,
    pizzaPrice: String
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterRes,
            contentDescription = "Pizza",
            modifier = Modifier
                .size(120.dp)
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

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = pizzaName,
                style = body_1_medium.copy(
                    color = textPrimary
                )
            )
            Text(
                text = pizzaDescription,
                style = body_3_regular.copy(
                    color = textSecondary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pizzaPrice,
                style = title_1_semiBold.copy(
                    color = textPrimary
                )
            )
        }
    }
}

@Preview
@Composable
fun PizzaCardPreview() {
    PizzaCard(
        painterRes = painterResource(id = R.drawable.trash_ic),
        pizzaName = "Margherita",
        pizzaDescription = "Tomato sauce, mozzarella, fresh basil, olive oil",
        pizzaPrice = "$8.99"
    )
}