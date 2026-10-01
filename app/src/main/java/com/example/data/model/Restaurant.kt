package com.example.data.model

data class Restaurant(
    val id: String, // e.g. WQE-RST-881230
    val name: String,
    val nameBn: String,
    val cuisine: String,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTimeMinutes: Int,
    val deliveryFeeBdt: Double,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val logoUrl: String,
    val coverUrl: String,
    val isOpen: Boolean = true,
    val isVerified: Boolean = true,
    val isFeatured: Boolean = false,
    val discountText: String? = null
)
