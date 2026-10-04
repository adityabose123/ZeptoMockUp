package com.example.zeptomockup.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
fun ZeptoApp(vm: CartViewModel) {
    var showCart by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showCart) { showCart = false }

    Box(modifier = Modifier.fillMaxSize().background(Color.White).navigationBarsPadding()) {
        if (showCart) {
            CartScreen(vm, onBack = { showCart = false })
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    HomeScreen(vm)
                    if (vm.itemCount > 0) {
                        CartBar(vm, onOpenCart = { showCart = true }, modifier = Modifier.align(Alignment.BottomCenter))
                    }
                }
                BottomNav()
            }
        }
    }
}

@Composable
private fun CartBar(vm: CartViewModel, onOpenCart: () -> Unit, modifier: Modifier = Modifier) {
    val remaining = vm.amountForFreeDelivery
    val progress = (vm.totalPrice.toFloat() / CartViewModel.FREE_DELIVERY_MIN).coerceIn(0f, 1f)
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                .padding(start = 14.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Offers", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ZeptoPink)
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = null, tint = ZeptoPink, modifier = Modifier.size(18.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, bottom = 10.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ZeptoDark)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp)) {
                    LinearProgressIndicator(
                        progress = { progress },
                        color = ZeptoGreen,
                        trackColor = Color(0xFF55555C),
                        modifier = Modifier.width(36.dp),
                    )
                    Text("🛵", fontSize = 20.sp, modifier = Modifier.padding(bottom = 18.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        if (remaining == 0) "Free delivery unlocked" else "Unlock free delivery",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                    Text(
                        if (remaining == 0) "You saved the delivery fee" else "Shop for ₹$remaining more",
                        color = Color(0xFFCFCFD4),
                        fontSize = 12.sp,
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(ZeptoPink)
                    .clickable(onClick = onOpenCart)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Cart", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    Text(
                        "${vm.itemCount} item${if (vm.itemCount > 1) "s" else ""}",
                        color = Color.White,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(0.5.dp, Color(0xFFEAEAEA))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        NavItem("Home", selected = true) {
            Icon(Icons.Filled.Home, contentDescription = null, tint = ZeptoPink)
        }
        NavItem("Categories") { Text("▦", fontSize = 22.sp, color = Color.Gray) }
        NavItem("Mobiles") { Text("📱", fontSize = 20.sp) }
        NavItem("Spotlight") {
            Box(Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFFFD43B)))
        }
        NavItem("select") {
            Text("select", fontWeight = FontWeight.Bold, color = Color(0xFFB4561F), fontSize = 15.sp)
        }
    }
}

@Composable
private fun NavItem(label: String, selected: Boolean = false, icon: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 6.dp)) {
        Box(Modifier.size(28.dp), contentAlignment = Alignment.Center) { icon() }
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) ZeptoPink else Color.DarkGray,
        )
    }
}
