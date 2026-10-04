package com.example.zeptomockup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zeptomockup.CartViewModel
import com.example.zeptomockup.data.Category
import com.example.zeptomockup.data.Product
import com.example.zeptomockup.data.ProductRepository

@Composable
fun HomeScreen(vm: CartViewModel, modifier: Modifier = Modifier) {
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }
    val searching = vm.query.isNotBlank()

    val products: List<Product> = when {
        searching -> ProductRepository.search(vm.query)
        selectedCategory != null -> ProductRepository.byCategory(selectedCategory!!)
        else -> ProductRepository.products
    }
    val title = when {
        searching -> "Results for \"${vm.query.trim()}\" (${products.size})"
        selectedCategory != null -> selectedCategory!!
        else -> "Best sellers"
    }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {
        Header(vm)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 96.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (!searching) {
                item(span = { GridItemSpan(maxLineSpan) }) { PromoBanner() }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CategoryRow(selectedCategory) { name ->
                        selectedCategory = if (selectedCategory == name) null else name
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            if (products.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("🔍", fontSize = 48.sp)
                        Text("No products found", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                        Text("Try searching for something else", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    quantity = vm.quantityOf(product),
                    onAdd = { vm.add(product) },
                    onRemove = { vm.remove(product) },
                )
            }
        }
    }
}

@Composable
private fun Header(vm: CartViewModel) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(ZeptoPurple, ZeptoPurpleLight)))
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text("Delivery in 10 minutes", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        Text("Home • Kolkata, West Bengal ▾", color = Color(0xFFE0D0F0), fontSize = 12.sp)
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (vm.query.isEmpty()) {
                    Text("Search for \"Garnier\"", color = Color.Gray, fontSize = 15.sp)
                }
                BasicTextField(
                    value = vm.query,
                    onValueChange = vm::updateQuery,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (vm.query.isNotEmpty()) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Clear search",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { vm.updateQuery("") },
                )
            }
        }
    }
}

@Composable
private fun PromoBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFFF4D8D), Color(0xFF8E2DE2))))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("FLAT 20% OFF", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
            Text("on beauty & hair care", color = Color.White, fontSize = 13.sp)
            Text(
                "Try searching \"Garnier\"",
                color = ZeptoPurple,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            )
        }
        Text("💄", fontSize = 52.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun CategoryRow(selected: String?, onSelect: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(ProductRepository.categories) { category ->
            CategoryItem(category, category.name == selected) { onSelect(category.name) }
        }
    }
}

@Composable
private fun CategoryItem(category: Category, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(category.colorHex)),
            contentAlignment = Alignment.Center,
        ) {
            Text(category.emoji, fontSize = 30.sp)
        }
        Text(
            category.name,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (selected) ZeptoPink else Color.Black,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
