package com.seno.core.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.presentation.R
import com.seno.core.presentation.theme.body_3_regular
import com.seno.core.presentation.theme.outline50
import com.seno.core.presentation.theme.textPrimary
import com.seno.core.presentation.theme.textSecondary
import com.seno.core.presentation.theme.title_2

@SuppressLint("DefaultLocale")
@Composable
fun ToppingCard(
    painterRes: Painter,
    toppingName: String,
    toppingPrice: Double
) {
    var quantity by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (quantity == 0) quantity = 1 }
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = outline50,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterRes,
            contentDescription = "Pizza",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape
                )
                .padding(12.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = toppingName,
            style = body_3_regular.copy(color = textSecondary)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (quantity == 0) {
            Text(
                text = "$${String.format("%.2f", toppingPrice)}",
                style = title_2.copy(color = textPrimary)
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (quantity > 1) quantity-- else quantity = 0
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
                        imageVector = ImageVector.vectorResource(R.drawable.minus_ic),
                        contentDescription = "Decrease",
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )

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
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview
@Composable
fun ToppingCardPreview() {
    ToppingCard(
        painterRes = painterResource(id = R.drawable.trash_ic),
        toppingName = "Sauce",
        toppingPrice = 1.00
    )
}
