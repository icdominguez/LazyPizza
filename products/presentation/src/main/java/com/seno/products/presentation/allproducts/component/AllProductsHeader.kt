package com.seno.products.presentation.allproducts.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seno.core.domain.product.ProductType
import com.seno.core.presentation.components.bar.CustomizableSearchBar
import com.seno.core.presentation.theme.LazyPizzaTheme

@Composable
fun AllProductsHeader(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onChipClick: (ProductType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
            query = searchQuery,
            onQueryChange = onQueryChange,
        )

        ProductFilterChips(
            onChipClick = onChipClick,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AllProductsHeaderPreview() {
    LazyPizzaTheme {
        AllProductsHeader(
            searchQuery = "",
            onChipClick = {
            },
            onQueryChange = {
            },
        )
    }
}
