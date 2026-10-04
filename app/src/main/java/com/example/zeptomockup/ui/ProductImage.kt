package com.example.zeptomockup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.zeptomockup.data.Product

/** Product photo when the product has one, otherwise (or while loading) its emoji tile. */
@Composable
fun ProductImage(
    product: Product,
    modifier: Modifier = Modifier,
    large: Boolean = false,
    emojiSize: TextUnit = 36.sp,
) {
    val url = if (large) product.imageLargeUrl ?: product.imageUrl else product.imageUrl
    Box(
        modifier = modifier.background(Color(product.colorHex)),
        contentAlignment = Alignment.Center,
    ) {
        if (url == null) {
            Text(product.emoji, fontSize = emojiSize)
        } else {
            SubcomposeAsyncImage(
                model = url,
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(6.dp),
                loading = { EmojiFallback(product, emojiSize) },
                error = { EmojiFallback(product, emojiSize) },
            )
        }
    }
}

@Composable
private fun EmojiFallback(product: Product, size: TextUnit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(product.emoji, fontSize = size)
    }
}
