package com.example.zeptomockup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zeptomockup.data.Product

@Composable
fun ProductCard(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFFEDEDED), RoundedCornerShape(14.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(product.colorHex)),
                contentAlignment = Alignment.Center,
            ) { Text(product.emoji, fontSize = 36.sp) }
            Icon(
                Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = ZeptoPink,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(18.dp),
            )
            AddButton(
                quantity = quantity,
                onAdd = onAdd,
                onRemove = onRemove,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp),
            )
        }
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                "₹${product.price}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ZeptoGreen)
                    .padding(horizontal = 7.dp, vertical = 2.dp),
            )
            if (product.mrp > product.price) {
                Text(
                    "₹${product.mrp}",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough,
                )
            }
        }
        if (product.savings > 0) {
            Text(
                "₹${product.savings} OFF",
                color = ZeptoGreen,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
        Text(
            product.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            minLines = 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(product.quantity, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
        Row(modifier = Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("★", color = ZeptoGreen, fontSize = 11.sp)
            Text(
                " ${product.rating}(${product.ratingCount})",
                color = Color.DarkGray,
                fontSize = 11.sp,
            )
        }
        product.offerText?.let {
            Text(
                "$it ›",
                color = Color(0xFF1565C0),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
fun AddButton(
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(10.dp)
    if (quantity == 0) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(Color.White)
                .border(2.dp, ZeptoPink, shape)
                .clickable(onClick = onAdd)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("ADD", color = ZeptoPink, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
    } else {
        Row(
            modifier = modifier
                .clip(shape)
                .background(ZeptoPink),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "−",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable(onClick = onRemove)
                    .width(30.dp)
                    .padding(vertical = 5.dp),
            )
            Text(
                "$quantity",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(18.dp),
            )
            Text(
                "+",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable(onClick = onAdd)
                    .width(30.dp)
                    .padding(vertical = 5.dp),
            )
        }
    }
}
