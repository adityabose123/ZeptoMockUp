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

    val savings: Int
        get() = mrp - price

    // Deterministic placeholder rating data for the mock catalogue.
    val rating: Double
        get() = 4.0 + (id * 7 % 10) / 10.0

    val ratingCount: Int
        get() = 40 + (id * 37 % 420)

    val offerText: String?
        get() = if (id % 7 == 0) "Buy 2 Get 1 Free" else null
}

data class Category(
    val name: String,
    val label: String,
    val tag: String,
    val emoji: String,
    val colorHex: Long,
)
