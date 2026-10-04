package com.example.zeptomockup.data

data class Product(
    val id: Int,
    val name: String,
    val brand: String,
    val quantity: String,
    val price: Int,
    val mrp: Int,
    val category: String,
    val emoji: String,
    val colorHex: Long,
) {
    val discountPercent: Int
        get() = if (mrp > price) ((mrp - price) * 100) / mrp else 0
}

data class Category(val name: String, val emoji: String, val colorHex: Long)
