package com.example.zeptomockup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.zeptomockup.data.Product
import com.example.zeptomockup.data.ProductRepository

class CartViewModel : ViewModel() {

    var query by mutableStateOf("")
        private set

    /** productId -> quantity */
    private val quantities = mutableStateMapOf<Int, Int>()

    fun updateQuery(value: String) {
        query = value
    }

    fun quantityOf(product: Product): Int = quantities[product.id] ?: 0

    fun add(product: Product) {
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
        get() = ProductRepository.products.mapNotNull { p ->
            quantities[p.id]?.let { p to it }
        }

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
