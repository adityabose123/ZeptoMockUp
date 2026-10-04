package com.example.zeptomockup.data

object ProductRepository {

    val categories = listOf(
        Category("Beauty", "Beauty &\nPersonal Care", "BUY 1 GET 1", "💄🧴", 0xFFFCE4EC),
        Category("Hair Care", "Hair Care\nEssentials", "UP TO 40% OFF", "🧴🌿", 0xFFEDE7F6),
        Category("Snacks", "Munch, Sip\n& Chill", "STARTING FROM ₹19", "🍟🥤", 0xFFFFF1D6),
        Category("Home Care", "Home\nRefresh", "STARTING FROM ₹29", "🧺🧹", 0xFFE6F0FB),
        Category("Dairy & Eggs", "Dairy, Bread\n& Eggs", "STARTING FROM ₹29", "🥛🍞", 0xFFE0F7FA),
        Category("Fruits & Veg", "Fresh Fruits\n& Veggies", "STARTING FROM ₹22", "🥦🍎", 0xFFE3F5E1),
    )

    val products: List<Product> = listOf(
        // Fruits & Veg
        Product(1, "Banana Robusta", "Fresh", "6 pcs", 39, 49, "Fruits & Veg", "🍌", 0xFFFFF8D6),
        Product(2, "Tomato Hybrid", "Fresh", "500 g", 22, 30, "Fruits & Veg", "🍅", 0xFFFFE5E2),
        Product(3, "Onion", "Fresh", "1 kg", 34, 45, "Fruits & Veg", "🧅", 0xFFFFEFD9),
        Product(4, "Apple Shimla", "Fresh", "4 pcs", 129, 160, "Fruits & Veg", "🍎", 0xFFFFE5E2),
        Product(5, "Potato", "Fresh", "1 kg", 29, 38, "Fruits & Veg", "🥔", 0xFFF5EBDD),
        Product(6, "Cucumber", "Fresh", "500 g", 25, 32, "Fruits & Veg", "🥒", 0xFFE3F5E1),
        // Dairy & Eggs
        Product(7, "Amul Taaza Milk", "Amul", "500 ml", 29, 29, "Dairy & Eggs", "🥛", 0xFFE6F0FB),
        Product(8, "Farm Fresh Eggs", "Eggoz", "6 pcs", 58, 72, "Dairy & Eggs", "🥚", 0xFFFFF8E7),
        Product(9, "Amul Butter", "Amul", "100 g", 58, 60, "Dairy & Eggs", "🧈", 0xFFFFF4C9),
        Product(10, "Fresh Paneer", "Amul", "200 g", 85, 95, "Dairy & Eggs", "🧀", 0xFFFFF8E7),
        Product(11, "Greek Yogurt", "Epigamia", "90 g", 45, 55, "Dairy & Eggs", "🍨", 0xFFF3E5F5),
        // Snacks
        Product(12, "Lay's Magic Masala", "Lay's", "52 g", 20, 20, "Snacks", "🍟", 0xFFFFF1D6),
        Product(13, "Kurkure Masala Munch", "Kurkure", "90 g", 20, 20, "Snacks", "🌽", 0xFFFFE9CC),
        Product(14, "Oreo Biscuits", "Cadbury", "120 g", 35, 40, "Snacks", "🍪", 0xFFE8E4F0),
        Product(15, "Dairy Milk Silk", "Cadbury", "60 g", 85, 90, "Snacks", "🍫", 0xFFEFE0F7),
        Product(16, "Maggi Noodles", "Nestle", "280 g", 56, 62, "Snacks", "🍜", 0xFFFFF1D6),
        // Beauty & Hair — Garnier
        Product(17, "Garnier Vitamin C Serum", "Garnier", "30 ml", 449, 599, "Beauty", "🍊", 0xFFFFEDD6),
        Product(18, "Garnier Micellar Water", "Garnier", "125 ml", 189, 249, "Beauty", "💧", 0xFFE0F2FE),
        Product(19, "Garnier Bright Complete Face Wash", "Garnier", "100 g", 165, 205, "Beauty", "🧼", 0xFFFFF3C4),
        Product(20, "Garnier Men Acno Fight Face Wash", "Garnier", "100 g", 149, 190, "Beauty", "🧴", 0xFFE0F7E9),
        Product(21, "Garnier Sunscreen SPF 50", "Garnier", "50 g", 299, 399, "Beauty", "☀️", 0xFFFFF0D2),
        Product(22, "Garnier Fructis Shampoo", "Garnier", "340 ml", 249, 335, "Hair Care", "🧴", 0xFFE3F5E1),
        Product(23, "Garnier Ultra Blends Conditioner", "Garnier", "180 ml", 175, 225, "Hair Care", "🌿", 0xFFE3F5E1),
        Product(24, "Garnier Color Naturals Hair Colour", "Garnier", "70 ml + 40 g", 199, 240, "Hair Care", "🎨", 0xFFEDE7F6),
        Product(25, "Garnier Black Naturals Hair Colour", "Garnier", "1 pack", 185, 220, "Hair Care", "🖤", 0xFFE8E8EE),
        Product(26, "Garnier Skin Naturals Moisturiser", "Garnier", "100 ml", 135, 170, "Beauty", "🧴", 0xFFFCE4EC),
        // Other beauty / hair
        Product(27, "Dove Hair Fall Shampoo", "Dove", "340 ml", 285, 360, "Hair Care", "🧴", 0xFFE6F0FB),
        Product(28, "Maybelline Kajal", "Maybelline", "0.35 g", 199, 249, "Beauty", "👁️", 0xFFEDE7F6),
        Product(29, "Nivea Body Lotion", "Nivea", "200 ml", 215, 280, "Beauty", "🧴", 0xFFE0F2FE),
        // Cold drinks
        Product(30, "Coca-Cola", "Coca-Cola", "750 ml", 40, 40, "Snacks", "🥤", 0xFFFFE5E2),
        Product(31, "Sprite", "Coca-Cola", "750 ml", 40, 40, "Snacks", "🥤", 0xFFE3F5E1),
        Product(32, "Real Mixed Fruit Juice", "Real", "1 L", 110, 120, "Snacks", "🧃", 0xFFFFEFD9),
        // Bakery
        Product(33, "Whole Wheat Bread", "Harvest Gold", "400 g", 45, 50, "Dairy & Eggs", "🍞", 0xFFFBE9D7),
        Product(34, "Butter Croissant", "Theobroma", "2 pcs", 99, 120, "Dairy & Eggs", "🥐", 0xFFFFF1D6),
        // Home care
        Product(35, "Surf Excel Detergent", "Surf Excel", "1 kg", 135, 160, "Home Care", "🧺", 0xFFE0F2FE),
        Product(36, "Vim Dishwash Gel", "Vim", "500 ml", 105, 125, "Home Care", "🍋", 0xFFFFF8D6),
    )

    val brandNames: Set<String> = products.map { it.brand.lowercase() }.toSet()

    fun search(query: String): List<Product> {
        val tokens = query.trim().lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (tokens.isEmpty()) return products
        return products.filter { p ->
            val haystack = "${p.name} ${p.brand} ${p.category}".lowercase()
            tokens.all { haystack.contains(it) }
        }
    }

    fun byCategory(name: String): List<Product> = products.filter { it.category == name }
}
