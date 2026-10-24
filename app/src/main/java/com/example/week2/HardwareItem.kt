package com.example.week2

import androidx.compose.runtime.Immutable
import java.text.Normalizer
import java.util.Locale

@Immutable
data class HardwareItem(
    val id: String,
    val name: String,
    val category: String,
    val cost: Double,
    val stockCount: Int
)

val initialHardwareItems = listOf(
    HardwareItem("101", "MacBook Pro M3 Max", "Geliştirici Bilgisayarı", 3499.0, 5),
    HardwareItem("102", "NVIDIA RTX 4090 Sunucu", "Yapay Zeka / GPU", 1599.0, 2),
    HardwareItem("103", "Google Pixel 8 Pro", "Mobil Test Cihazı", 999.0, 8),
    HardwareItem("104", "Dell UltraSharp 49\"", "Monitör", 1199.0, 4),
    HardwareItem("105", "Sony WH-1000XM5 Kulaklık", "Odaklanma Ekipmanı", 399.0, 12),
    HardwareItem("106", "Raspberry Pi 5 8GB", "IoT Geliştirme Kiti", 85.0, 25),
    HardwareItem("107", "HHKB Professional Keyboard", "Ergonomik Klavye", 299.0, 0),
    HardwareItem("108", "AMD Ryzen Threadripper", "Yapay Zeka / GPU", 4999.0, 1)
)

// Locale-independent search also accepts Turkish text entered without diacritics.
private fun String.searchKey(): String = Normalizer.normalize(this, Normalizer.Form.NFD)
    .replace(Regex("\\p{M}+"), "")
    .lowercase(Locale.ROOT)
    .replace('ı', 'i')

fun filterHardware(
    items: List<HardwareItem>,
    searchQuery: String,
    selectedCategory: String?
): List<HardwareItem> {
    val query = searchQuery.trim().searchKey()
    return items.filter { item ->
        (selectedCategory == null || item.category == selectedCategory) &&
            (item.name.searchKey().contains(query) || item.category.searchKey().contains(query))
    }
}

// Returning the same list for a rejected allocation makes it a true no-op.
fun allocateHardware(items: List<HardwareItem>, id: String): List<HardwareItem> {
    val item = items.find { it.id == id } ?: return items
    if (item.stockCount <= 0) return items
    return items.map { if (it.id == id) it.copy(stockCount = it.stockCount - 1) else it }
}

fun totalSpentFor(items: List<HardwareItem>): Double = items.sumOf { item ->
    val original = initialHardwareItems.first { it.id == item.id }
    (original.stockCount - item.stockCount) * item.cost
}
