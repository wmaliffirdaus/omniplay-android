package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "studios")
data class Studio(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val rating: Double,
    val reviewCount: Int,
    val address: String,
    val distanceText: String,
    val imageUrl: String,
    val startingPrice: Double,
    val featuresString: String, // Comma separated, e.g. "Fiber 10Gbps,RTX 4090 Rigs,240Hz OLED"
    val category: String // "PS5", "PC", "VR", "Racing"
) {
    val featuresList: List<String>
        get() = featuresString.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

@Entity(tableName = "stations")
data class Station(
    @PrimaryKey val id: Int,
    val studioId: Int,
    val name: String,
    val type: String, // "PC", "Console"
    val pricePerHour: Double,
    val status: String // "Available", "Booked"
)

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studioId: Int,
    val studioName: String,
    val stationId: Int,
    val stationName: String,
    val dateText: String,
    val timeText: String,
    val studioLocation: String,
    val pricePerHour: Double,
    val totalAmount: Double,
    val qrCodePayload: String,
    val isConfirmed: Boolean = true,
    val isCancelled: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val category: String, // "SYSTEM ALERT", "OMNIELITE", "PROMOTION"
    val timeText: String,
    val isRead: Boolean = false
)
