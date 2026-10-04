package com.example.zeptomockup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zeptomockup.CartViewModel

@Composable
fun CartScreen(vm: CartViewModel, onBack: () -> Unit, modifier: Modifier = Modifier) {
    var orderPlaced by remember { mutableStateOf(false) }
    val cartItems = vm.cartItems

    Column(modifier = modifier.fillMaxSize().background(ZeptoBackground)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable(onClick = onBack),
            )
            Text("Cart", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 12.dp))
        }

        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("🛒", fontSize = 64.sp)
                Text("Your cart is empty", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                Text("Add items to get started", color = Color.Gray)
                Text(
                    "Browse products",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ZeptoPink)
                        .clickable(onClick = onBack)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (vm.totalSavings > 0) {
                    item {
                        Text(
                            "🎉 You're saving ₹${vm.totalSavings} on this order",
                            color = ZeptoGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFE3F5E1))
                                .padding(12.dp),
                        )
                    }
                }
                item {
                    Text(
                        "Delivery in 10 minutes • ${vm.itemCount} item(s)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(12.dp),
                    )
                }
                items(cartItems, key = { it.first.id }) { (product, qty) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { vm.openProductId = product.id; onBack() }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ProductImage(
                            product = product,
                            emojiSize = 28.sp,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(10.dp)),
                        )
                        Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                            Text(product.name, fontWeight = FontWeight.Medium, fontSize = 13.sp, maxLines = 2)
                            Text(product.quantity, color = Color.Gray, fontSize = 11.sp)
                            Text("₹${product.price * qty}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        AddButton(
                            quantity = qty,
                            onAdd = { vm.add(product) },
                            onRemove = { vm.remove(product) },
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text("Bill details", fontWeight = FontWeight.Bold)
                        BillRow("Item total", "₹${vm.totalPrice}")
                        BillRow("Delivery fee", if (vm.deliveryFee == 0) "FREE" else "₹${vm.deliveryFee}", valueColor = if (vm.deliveryFee == 0) ZeptoGreen else Color.Unspecified)
                        BillRow("Handling fee", "₹${CartViewModel.HANDLING_FEE}")
                        Spacer(Modifier.size(2.dp))
                        BillRow("To pay", "₹${vm.totalPay}", bold = true)
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ZeptoPink)
                        .clickable { orderPlaced = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("₹${vm.totalPay}  TOTAL", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Place Order ▸", color = Color.White, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }

    if (orderPlaced) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Order placed! 🎉") },
            text = { Text("Your order will arrive in 10 minutes.") },
            confirmButton = {
                TextButton(onClick = {
                    orderPlaced = false
                    vm.clear()
                    onBack()
                }) { Text("OK") }
            },
        )
    }
}

@Composable
private fun BillRow(label: String, value: String, bold: Boolean = false, valueColor: Color = Color.Unspecified) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontSize = 13.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal, color = valueColor)
    }
}
