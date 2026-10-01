package com.example.data.model

data class User(
    val id: String, // e.g. WQE-CUS-102934 or WQE-RDR-492019
    val name: String,
    val phone: String,
    val email: String,
    val role: UserRole,
    val avatarUrl: String = "",
    val points: Int = 120,
    val rating: Double = 4.9,
    val defaultAddress: String = "House 42, Road 11, Banani, Dhaka",
    val defaultLat: Double = 23.7937,
    val defaultLng: Double = 90.4066,
    val token: String = "token_session_${System.currentTimeMillis()}",
    val isVerified: Boolean = true,
    val language: String = "bn"
)
