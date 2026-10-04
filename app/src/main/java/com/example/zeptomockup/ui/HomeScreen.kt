package com.example.zeptomockup.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
    var searchActive by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }
    val resultsMode = searchActive || selectedCategory != null

    fun exitResults() {
        searchActive = false
        selectedCategory = null
        vm.updateQuery("")
    }

    BackHandler(enabled = resultsMode) { exitResults() }

    if (resultsMode) {
        ResultsScreen(
            vm = vm,
            category = selectedCategory,
            onBack = ::exitResults,
            modifier = modifier,
        )
    } else {
        LandingScreen(
            vm = vm,
            onSearchClick = { searchActive = true },
            onCategoryClick = { selectedCategory = it },
            modifier = modifier,
        )
    }
}

@Composable
private fun LandingScreen(
    vm: CartViewModel,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ZeptoYellow, Color.White), endY = 1500f)),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 160.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) { LocationRow() }
            item(span = { GridItemSpan(maxLineSpan) }) { SearchEntry(onSearchClick) }
            item(span = { GridItemSpan(maxLineSpan) }) { FestBanner() }
            items(ProductRepository.categories) { category ->
                CategoryTile(category) { onCategoryClick(category.name) }
            }
            item(span = { GridItemSpan(maxLineSpan) }) { PromoStrip() }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Explore", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFE0E0E0)))
                }
            }
            items(ProductRepository.products, key = { it.id }) { product ->
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
private fun LocationRow() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text("⚡ 6 minutes", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Home - Flat 104, first floor, Kolkata",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
        Text(
            "REFER &\nEARN",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF4A2A9B))
                .padding(horizontal = 10.dp, vertical = 6.dp),
        )
        Spacer(Modifier.width(10.dp))
        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun SearchEntry(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(10.dp))
        Text("Search \"Garnier\"", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFF4E0))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("🪔", fontSize = 20.sp)
            Text(
                " Diwali\n Specials",
                color = Color(0xFF1565C0),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 14.sp,
            )
        }
    }
}

@Composable
private fun FestBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFFD62828), Color(0xFF9D0208))))
                .border(3.dp, Color(0xFFFFC53D), RoundedCornerShape(22.dp))
                .padding(horizontal = 36.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("GRAND", color = Color(0xFFFFC53D), fontWeight = FontWeight.Black, fontSize = 30.sp, lineHeight = 30.sp)
            Text("SHOPPING FEST", color = Color(0xFFFFC53D), fontWeight = FontWeight.Black, fontSize = 26.sp, lineHeight = 28.sp)
            Text(
                "30TH SEP - 6TH OCT",
                color = Color(0xFF9D0208),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFC53D))
                    .padding(horizontal = 12.dp, vertical = 3.dp),
            )
        }
        Text("🪙", fontSize = 40.sp, modifier = Modifier.align(Alignment.CenterStart))
        Text("🪙", fontSize = 40.sp, modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
private fun CategoryTile(category: Category, onClick: () -> Unit) {
    val red = Color(0xFFC62828)
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEFE4E4), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            category.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 15.sp,
            modifier = Modifier.padding(top = 10.dp, start = 4.dp, end = 4.dp),
        )
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(width = 84.dp, height = 56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(category.colorHex)),
            contentAlignment = Alignment.Center,
        ) { Text(category.emoji, fontSize = 28.sp) }
        Text(
            category.tag,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .background(red)
                .padding(vertical = 5.dp),
        )
    }
}

@Composable
private fun PromoStrip() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        val promos = listOf(
            "Get 1kg free sugar" to "on the purchase of eligible items",
            "Free delivery" to "on orders above ₹${CartViewModel.FREE_DELIVERY_MIN}",
            "Flat ₹100 off" to "on Garnier skincare",
        )
        items(promos.size) { i ->
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFFBEA))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(promos[i].first, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text(promos[i].second, fontSize = 13.sp, color = Color.DarkGray)
                }
                Text(
                    "›",
                    fontSize = 22.sp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                )
            }
        }
    }
}

@Composable
private fun ResultsScreen(
    vm: CartViewModel,
    category: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(category) {
        if (category == null) focusRequester.requestFocus()
    }

    val products: List<Product> = when {
        category != null -> ProductRepository.byCategory(category)
        else -> ProductRepository.search(vm.query)
    }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFEDEDED), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable(onClick = onBack),
                )
                Spacer(Modifier.width(14.dp))
                if (category != null) {
                    Text(category, fontSize = 18.sp, modifier = Modifier.weight(1f))
                } else {
                    Box(modifier = Modifier.weight(1f)) {
                        if (vm.query.isEmpty()) {
                            Text("Search \"Garnier\"", color = Color.Gray, fontSize = 18.sp)
                        }
                        BasicTextField(
                            value = vm.query,
                            onValueChange = vm::updateQuery,
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        )
                    }
                    if (vm.query.isNotEmpty()) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Clear search",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .clickable { vm.updateQuery("") }
                                .padding(4.dp),
                        )
                    }
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFFBE8D3))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text("select", color = Color(0xFFB4561F), fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 18.sp)
                Text("FINDS", color = Color(0xFFB4561F), fontWeight = FontWeight.Bold, fontSize = 9.sp, lineHeight = 9.sp)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 160.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (products.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
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
