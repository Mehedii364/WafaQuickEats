package com.example.data.model

data class SupportTicket(
    val id: String = "TKT-${System.currentTimeMillis() % 100000}",
    val userId: String,
    val userRole: UserRole,
    val orderId: String? = null,
    val category: String, // Order Issue, Payment, App Bug, Rider Behavior, Food Quality
    val subject: String,
    val message: String,
    val priority: String = "Normal", // Low, Normal, High, Urgent
    val status: String = "Open", // Open, In Review, Waiting, Resolved, Closed
    val reply: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class PointLedgerEntry(
    val id: String = "PTS-${System.currentTimeMillis() % 100000}",
    val userId: String,
    val role: UserRole,
    val amount: Int, // e.g. +25 or -50
    val reason: String,
    val orderId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class Invoice(
    val invoiceNumber: String, // WQE-INV-YYYYMMDD-000125
    val orderId: String,
    val date: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val restaurantName: String,
    val restaurantAddress: String,
    val items: List<CartItem>,
    val subtotalBdt: Double,
    val deliveryFeeBdt: Double,
    val discountBdt: Double,
    val vatTaxBdt: Double,
    val totalBdt: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val riderName: String? = null
)

data class AiProviderConfig(
    val id: String,
    val name: String,
    val category: String, // FREE or PREMIUM
    val endpoint: String,
    val model: String,
    val apiKeyMasked: String,
    val isActive: Boolean = false,
    val isHealthy: Boolean = true,
    val latencyMs: Long = 180,
    val lastTested: String = "Just now"
)
