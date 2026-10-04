package com.example.zeptomockup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zeptomockup.CartViewModel
import com.example.zeptomockup.data.Product

@Composable
fun ProductDetailScreen(
    product: Product,
    vm: CartViewModel,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    onOpenProduct: (Product) -> Unit,
) {
    val quantity = vm.quantityOf(product)
    val similar = vm.catalog
        .filter { it.id != product.id && (it.brand == product.brand || it.category == product.category) }
        .sortedByDescending { it.brand == product.brand }
        .distinctBy { it.id }
        .take(10)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) { detectTapGestures { } },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable(onClick = onBack),
            )
            Text(
                product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
            )
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier.clickable(onClick = onOpenCart),
            )
            if (vm.itemCount > 0) {
                Text(
                    "${vm.itemCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ZeptoPink)
                        .padding(horizontal = 6.dp, vertical = 1.dp),
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
        ) {
            ProductImage(
                product = product,
                large = true,
                emojiSize = 96.sp,
                modifier = Modifier.fillMaxWidth().height(300.dp),
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "⚡ Delivery in 10 minutes",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZeptoPurple,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ZeptoYellow)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                )
                Text(
                    product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text("${product.brand} • ${product.quantity}", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
                Row(modifier = Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("★", color = ZeptoGreen, fontSize = 14.sp)
                    Text(
                        " ${product.rating} (${product.ratingCount} ratings)",
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "₹${product.price}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(ZeptoGreen)
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                    )
                    if (product.mrp > product.price) {
                        Text("₹${product.mrp}", color = Color.Gray, textDecoration = TextDecoration.LineThrough)
                        Text(
                            "₹${product.savings} OFF",
                            color = ZeptoGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                        )
                    }
                }
                Text("Inclusive of all taxes", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))

                Section("Product details") {
                    DetailRow("Brand", product.brand)
                    DetailRow("Pack size", product.quantity)
                    DetailRow("Category", product.category)
                    DetailRow("Rating", "${product.rating} / 5")
                }
                Section("About this product") {
                    Text(product.about, fontSize = 13.sp, lineHeight = 19.sp, color = Color.DarkGray)
                }
                if (product.ingredients.isNotBlank()) {
                    Section("Ingredients") {
                        Text(product.ingredients, fontSize = 12.sp, lineHeight = 18.sp, color = Color.DarkGray)
                    }
                }
            }

            if (similar.isNotEmpty()) {
                Text(
                    "Similar products",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                LazyRow(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(similar, key = { it.id }) { p ->
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .clickable { onOpenProduct(p) },
                        ) {
                            ProductImage(
                                product = p,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFEDEDED), RoundedCornerShape(12.dp)),
                            )
                            Text(
                                "₹${p.price}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 6.dp),
                            )
                            Text(p.name, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }

            Text(
                "Prices and ratings are illustrative. Product names, photos and ingredients from Open Beauty Facts / Open Food Facts (open data, ODbL / CC BY-SA).",
                color = Color.Gray,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(16.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, Color(0xFFEAEAEA))
                .background(Color.White)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (quantity == 0) {
                Text(
                    "Add to cart",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ZeptoPink)
                        .clickable { vm.add(product) }
                        .padding(vertical = 14.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            } else {
                AddButton(
                    quantity = quantity,
                    onAdd = { vm.add(product) },
                    onRemove = { vm.remove(product) },
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "View cart • ₹${vm.totalPrice}",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ZeptoPink)
                        .clickable(onClick = onOpenCart)
                        .padding(vertical = 12.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.width(100.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
    }
}
