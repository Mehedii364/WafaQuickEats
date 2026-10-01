package com.example.data.model

data class RiderNote(
    val id: String = "NOTE-${System.currentTimeMillis()}",
    val riderId: String,
    val orderId: String,
    val title: String,
    val details: String,
    val category: String = "Delivery", // Pickup, Delivery, Location, Road Issue, Customer, Delay, etc.
    val priority: String = "Normal", // Normal, Important
    val privacy: String = "Customer Visible", // Admin Only, Customer Visible, Rider Visible
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class DeliveryIncident(
    val id: String = "INC-${System.currentTimeMillis()}",
    val riderId: String,
    val orderId: String,
    val type: String, // Road blockage, Customer unavailable, Wrong location, Restaurant delay, Payment problem
    val description: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Open", // Open, In Review, Resolved
    val resolution: String = ""
)
