package com.example.data.model

data class Order(
    val id: String, // e.g. WQE-ORD-20260930-8910
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val restaurantId: String,
    val restaurantName: String,
    val restaurantPhone: String = "+8801711000000",
    val restaurantAddress: String = "Road 11, Banani, Dhaka",
    val restaurantLat: Double = 23.7937,
    val restaurantLng: Double = 90.4066,
    val riderId: String? = null,
    val riderName: String? = null,
    val riderPhone: String? = null,
    val riderLat: Double? = null,
    val riderLng: Double? = null,
    val items: List<CartItem>,
    val subtotalBdt: Double,
    val deliveryFeeBdt: Double,
    val discountBdt: Double = 0.0,
    val vatTaxBdt: Double = 0.0,
    val totalBdt: Double,
    val status: OrderStatus = OrderStatus.PENDING,
    val deliveryAddress: String,
    val customerLat: Double,
    val customerLng: Double,
    val deliveryInstructions: String = "",
    val paymentMethod: String = "Cash on Delivery", // bKash, Nagad, Rocket, Card, COD
    val paymentStatus: String = "Pending", // Pending, Paid, Refunded
    val otp: String = "4819", // Real 4-digit OTP for delivery verification
    val estimatedMinutes: Int = 25,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
