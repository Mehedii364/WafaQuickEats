package com.example.data.model

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    RIDER_ASSIGNED,
    PICKED_UP,
    ON_THE_WAY,
    NEAR_CUSTOMER,
    ARRIVED,
    DELIVERED,
    CANCELLED;

    val displayTitle: String
        get() = when (this) {
            PENDING -> "Order Placed (অর্ডার গৃহীত)"
            CONFIRMED -> "Order Confirmed (নিশ্চিত করা হয়েছে)"
            PREPARING -> "Kitchen Preparing (রান্না হচ্ছে)"
            READY_FOR_PICKUP -> "Food Ready (খাবার প্রস্তুত)"
            RIDER_ASSIGNED -> "Rider Assigned (রাইডার বরাদ্দ)"
            PICKED_UP -> "Picked Up (খাবার তোলা হয়েছে)"
            ON_THE_WAY -> "Rider on the way (রাইডার পথে)"
            NEAR_CUSTOMER -> "Near Your Door (কাছাকাছি পৌঁছেছে)"
            ARRIVED -> "Rider Arrived (দরজায় পৌঁছেছে)"
            DELIVERED -> "Delivered (সফল ডেলিভারি)"
            CANCELLED -> "Cancelled (বাতিল)"
        }

    val stepIndex: Int
        get() = when (this) {
            PENDING -> 0
            CONFIRMED -> 1
            PREPARING -> 2
            READY_FOR_PICKUP -> 3
            RIDER_ASSIGNED -> 4
            PICKED_UP -> 5
            ON_THE_WAY -> 6
            NEAR_CUSTOMER -> 7
            ARRIVED -> 8
            DELIVERED -> 9
            CANCELLED -> -1
        }
}
