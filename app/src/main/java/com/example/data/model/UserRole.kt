package com.example.data.model

enum class UserRole {
    CUSTOMER,
    RIDER,
    RESTAURANT,
    ADMIN;

    val displayName: String
        get() = when (this) {
            CUSTOMER -> "Customer (গ্রাহক)"
            RIDER -> "Rider (ডেলিভারি রাইডার)"
            RESTAURANT -> "Restaurant (রেস্তোরাঁ)"
            ADMIN -> "Admin (অ্যাডমিন)"
        }
}
