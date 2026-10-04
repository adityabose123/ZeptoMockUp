package com.example.zeptomockup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zeptomockup.data.Product
import com.example.zeptomockup.data.ProductRepository
import com.example.zeptomockup.data.RemoteCatalog
import com.example.zeptomockup.data.RemoteCatalog.Source
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private class Featured(val source: Source, val query: String, val category: String, val limit: Int)

    // ---- navigation / search state (kept here so it survives screen changes) ----
    var query by mutableStateOf("")
        private set
    var searchActive by mutableStateOf(false)
        private set
    var selectedCategory by mutableStateOf<String?>(null)
        private set
    var openProductId by mutableStateOf<Int?>(null)

    val resultsMode: Boolean
        get() = searchActive || selectedCategory != null

    // ---- catalogue ----
    var remoteProducts by mutableStateOf<List<Product>>(emptyList())
        private set
    private var searchResults by mutableStateOf<List<Product>>(emptyList())
    var searchLoading by mutableStateOf(false)
        private set
    private var searchJob: Job? = null

    /** productId -> quantity */
    private val quantities = mutableStateMapOf<Int, Int>()
    private val known = HashMap<Int, Product>()

    init {
        ProductRepository.products.forEach { known[it.id] = it }
        loadFeatured()
    }

    val catalog: List<Product>
        get() = remoteProducts + ProductRepository.products

    fun find(id: Int): Product? = known[id]

    private fun loadFeatured() {
        val featured = listOf(
            Featured(Source.BEAUTY, "garnier", "Beauty", 24),
            Featured(Source.BEAUTY, "nivea", "Beauty", 8),
            Featured(Source.BEAUTY, "shampoo", "Hair Care", 12),
            Featured(Source.FOOD, "lays", "Snacks", 8),
            Featured(Source.FOOD, "kurkure", "Snacks", 6),
            Featured(Source.FOOD, "amul", "Dairy & Eggs", 10),
        )
        featured.forEach { f ->
            viewModelScope.launch {
                val r = RemoteCatalog.search(f.source, f.query, f.category, f.limit)
                if (r.isNotEmpty()) {
                    r.forEach { known[it.id] = it }
                    remoteProducts = (remoteProducts + r).distinctBy { it.id }
                }
            }
        }
    }

    fun categoryProducts(name: String): List<Product> = catalog.filter { it.category == name }

    val searchResultList: List<Product>
        get() {
            val local = ProductRepository.filter(remoteProducts, query)
            return if (searchResults.isNotEmpty()) {
                (searchResults + local).distinctBy { it.id }
            } else {
                (local + ProductRepository.search(query)).distinctBy { it.id }
            }
        }

    fun openSearch() {
        searchActive = true
    }

    fun selectCategory(name: String) {
        selectedCategory = name
    }

    fun exitResults() {
        searchActive = false
        selectedCategory = null
        updateQuery("")
    }

    fun updateQuery(value: String) {
        query = value
        searchJob?.cancel()
        if (value.isBlank()) {
            searchResults = emptyList()
            searchLoading = false
            return
        }
        searchJob = viewModelScope.launch {
            searchLoading = true
            delay(400)
            val found = coroutineScope {
                val beauty = async { RemoteCatalog.search(Source.BEAUTY, value.trim(), null, 24) }
                val food = async { RemoteCatalog.search(Source.FOOD, value.trim(), null, 24) }
                beauty.await() + food.await()
            }
            found.forEach { known[it.id] = it }
            searchResults = found.distinctBy { it.id }
            searchLoading = false
        }
    }

    // ---- cart ----
    fun quantityOf(product: Product): Int = quantities[product.id] ?: 0

    fun add(product: Product) {
        known[product.id] = product
        quantities[product.id] = quantityOf(product) + 1
    }

    fun remove(product: Product) {
        val q = quantityOf(product)
        if (q <= 1) quantities.remove(product.id) else quantities[product.id] = q - 1
    }

    fun clear() = quantities.clear()

    val itemCount: Int
        get() = quantities.values.sum()

    val cartItems: List<Pair<Product, Int>>
        get() = quantities.entries.mapNotNull { (id, q) -> known[id]?.let { it to q } }

    val totalPrice: Int
        get() = cartItems.sumOf { (p, q) -> p.price * q }

    val totalSavings: Int
        get() = cartItems.sumOf { (p, q) -> (p.mrp - p.price) * q }

    val deliveryFee: Int
        get() = if (itemCount == 0 || totalPrice >= FREE_DELIVERY_MIN) 0 else DELIVERY_FEE

    val totalPay: Int
        get() = totalPrice + deliveryFee + HANDLING_FEE

    val amountForFreeDelivery: Int
        get() = (FREE_DELIVERY_MIN - totalPrice).coerceAtLeast(0)

    companion object {
        const val FREE_DELIVERY_MIN = 199
        const val DELIVERY_FEE = 25
        const val HANDLING_FEE = 5
    }
}
