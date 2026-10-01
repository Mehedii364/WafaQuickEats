package com.example.data.model

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1,
    val selectedVariant: FoodVariant? = null,
    val selectedAddons: List<FoodAddon> = emptyList(),
    val specialInstructions: String = ""
) {
    val unitPrice: Double
        get() {
            val base = foodItem.discountPriceBdt ?: foodItem.priceBdt
            val variantExtra = selectedVariant?.extraPriceBdt ?: 0.0
            val addonsTotal = selectedAddons.sumOf { it.priceBdt }
            return base + variantExtra + addonsTotal
        }

    val totalPrice: Double
        get() = unitPrice * quantity
}
