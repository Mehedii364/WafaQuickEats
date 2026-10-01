package com.example.data.model

data class FoodVariant(
    val id: String,
    val name: String,
    val extraPriceBdt: Double
)

data class FoodAddon(
    val id: String,
    val name: String,
    val priceBdt: Double
)

data class FoodItem(
    val id: String,
    val restaurantId: String,
    val name: String,
    val nameBn: String,
    val description: String,
    val priceBdt: Double,
    val discountPriceBdt: Double? = null,
    val category: String,
    val imageUrl: String,
    val isAvailable: Boolean = true,
    val preparationTimeMinutes: Int = 15,
    val variants: List<FoodVariant> = emptyList(),
    val addons: List<FoodAddon> = emptyList(),
    val rating: Double = 4.8,
    val reviewCount: Int = 45
)
