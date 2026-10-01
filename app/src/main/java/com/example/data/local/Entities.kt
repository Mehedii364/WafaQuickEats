package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gps_queue")
data class GpsLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: String,
    val riderId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val speed: Float,
    val bearing: Float,
    val timestamp: Long,
    val isSynced: Boolean = false
)

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val foodId: String,
    val restaurantId: String,
    val foodName: String,
    val foodPrice: Double,
    val quantity: Int,
    val imageUrl: String,
    val selectedVariant: String = "",
    val selectedAddons: String = "",
    val specialInstructions: String = ""
)

@Entity(tableName = "orders_cache")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val customerId: String,
    val customerName: String,
    val restaurantId: String,
    val restaurantName: String,
    val totalBdt: Double,
    val status: String,
    val deliveryAddress: String,
    val paymentMethod: String,
    val paymentStatus: String,
    val otp: String,
    val riderId: String?,
    val riderName: String?,
    val createdAt: Long,
    val itemsJson: String
)

@Entity(tableName = "rider_notes")
data class RiderNoteEntity(
    @PrimaryKey val id: String,
    val riderId: String,
    val orderId: String,
    val title: String,
    val details: String,
    val category: String,
    val priority: String,
    val privacy: String,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Long
)

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val role: String,
    val orderId: String?,
    val category: String,
    val subject: String,
    val message: String,
    val priority: String,
    val status: String,
    val reply: String?,
    val createdAt: Long
)
