package com.example.zeptomockup.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zeptomockup.data.Product

/** Bundled product photo when the product has one, otherwise its emoji tile. */
@Composable
fun ProductImage(
    product: Product,
    modifier: Modifier = Modifier,
    emojiSize: TextUnit = 36.sp,
) {
    Box(
        modifier = modifier.background(Color(product.colorHex)),
        contentAlignment = Alignment.Center,
    ) {
        val res = product.imageRes
        if (res == null) {
            Text(product.emoji, fontSize = emojiSize)
        } else {
            Image(
                painter = painterResource(res),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(4.dp),
            )
        }
    }
}
