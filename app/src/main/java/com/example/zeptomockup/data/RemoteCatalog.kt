package com.example.zeptomockup.data

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * Real product data (names, brands, pack sizes, photos, ingredients) from the open
 * Open Beauty Facts / Open Food Facts databases. Neither database has prices, so
 * prices are generated deterministically per product and are illustrative only.
 */
object RemoteCatalog {

    enum class Source(val host: String, val defaultCategory: String) {
        BEAUTY("world.openbeautyfacts.org", "Beauty"),
        FOOD("world.openfoodfacts.org", "Snacks"),
    }

    private const val FIELDS =
        "code,product_name,brands,quantity,image_front_small_url,image_front_url,generic_name,ingredients_text"

    suspend fun search(
        source: Source,
        query: String,
        category: String? = null,
        limit: Int = 20,
    ): List<Product> = withContext(Dispatchers.IO) {
        try {
            val q = URLEncoder.encode(query, "UTF-8")
            val url = URL(
                "https://${source.host}/cgi/search.pl?search_terms=$q&search_simple=1" +
                    "&action=process&json=1&page_size=$limit&fields=$FIELDS",
            )
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 8000
            conn.readTimeout = 15000
            conn.setRequestProperty("User-Agent", "ZeptoMockUp/1.0 (educational mockup)")
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            parse(JSONObject(body).optJSONArray("products"), category ?: source.defaultCategory)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parse(array: JSONArray?, category: String): List<Product> {
        if (array == null) return emptyList()
        val out = ArrayList<Product>()
        for (i in 0 until array.length()) {
            val o = array.optJSONObject(i) ?: continue
            val code = o.optString("code")
            val name = o.optString("product_name").trim()
            val small = o.optString("image_front_small_url").ifBlank { o.optString("image_front_url") }
            val large = o.optString("image_front_url").ifBlank { small }
            if (code.isBlank() || name.isBlank() || small.isBlank()) continue
            val brand = o.optString("brands").split(",").firstOrNull()?.trim().orEmpty().ifBlank { "Generic" }
            val quantity = o.optString("quantity").trim().ifBlank { "1 pc" }
            val h = (code.hashCode() and 0x7fffffff)
            val (lo, hi) = when (category) {
                "Beauty", "Hair Care" -> 99 to 499
                "Snacks" -> 10 to 120
                else -> 25 to 180
            }
            val price = lo + h % (hi - lo)
            val mrp = price + price * (10 + h % 25) / 100
            out.add(
                Product(
                    id = h % 1_000_000_000 + 1000,
                    name = name,
                    brand = brand,
                    quantity = quantity,
                    price = price,
                    mrp = mrp,
                    category = category,
                    emoji = when (category) {
                        "Beauty" -> "🧴"
                        "Hair Care" -> "🧴"
                        "Dairy & Eggs" -> "🥛"
                        else -> "🛍️"
                    },
                    colorHex = 0xFFFFFFFF,
                    imageUrl = small,
                    imageLargeUrl = large,
                    description = o.optString("generic_name").trim(),
                    ingredients = o.optString("ingredients_text").trim(),
                ),
            )
        }
        return out
    }
}
