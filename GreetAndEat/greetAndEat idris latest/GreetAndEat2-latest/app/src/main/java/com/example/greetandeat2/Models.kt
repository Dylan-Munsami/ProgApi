package com.example.greetandeat2

import android.os.Parcelable
import com.example.greetandeat2.OrderStatus.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val id: String,
    val name: String,
    val price: Double,
    var quantity: Int = 1 // 👈 new field
) : Parcelable

data class Restaurant(
    val id: String,
    val name: String,
    val menu: List<MenuItem>
)

enum class OrderStatus {
    PENDING, CONFIRMED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
}
data class Order(
    val id: String? = null,
    val userId: String,
    val items: List<MenuItem>,
    val total: Double,
    val status: String = "ORDER_RECEIVED",
    val createdAt: String? = null,
    val estimatedDelivery: String? = null,
    val statusHistory: List<StatusUpdate> = emptyList()
)

data class StatusUpdate(
    val status: String,
    val timestamp: String,
    val message: String
)


