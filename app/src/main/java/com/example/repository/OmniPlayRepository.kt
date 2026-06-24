package com.example.repository

import com.example.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class OmniPlayRepository(private val database: AppDatabase) {

    private val studioDao = database.studioDao()
    private val stationDao = database.stationDao()
    private val bookingDao = database.bookingDao()
    private val notificationDao = database.notificationDao()

    val allStudios: Flow<List<Studio>> = studioDao.getAllStudios()
    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings()
    val allNotifications: Flow<List<Notification>> = notificationDao.getAllNotifications()

    fun getStudiosByCategory(category: String): Flow<List<Studio>> {
        return if (category == "All") {
            studioDao.getAllStudios()
        } else {
            studioDao.getStudiosByCategory(category)
        }
    }

    fun getStationsForStudio(studioId: Int): Flow<List<Station>> =
        stationDao.getStationsForStudio(studioId)

    suspend fun getStudioById(id: Int): Studio? = studioDao.getStudioById(id)

    suspend fun insertBooking(booking: Booking): Long {
        return bookingDao.insertBooking(booking)
    }

    suspend fun updateBooking(booking: Booking) {
        bookingDao.updateBooking(booking)
    }

    suspend fun deleteBooking(id: Int) {
        bookingDao.deleteBookingById(id)
    }

    suspend fun markAllNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }

    suspend fun addNotification(notification: Notification) {
        notificationDao.insertNotification(notification)
    }

    suspend fun updateStationStatus(station: Station) {
        stationDao.updateStation(station)
    }

    suspend fun seedIfNeeded() {
        // Seed Studios if database is empty
        val existingStudios = studioDao.getAllStudios().first()
        if (existingStudios.isEmpty()) {
            val studios = listOf(
                Studio(
                    id = 1,
                    name = "CTRL+PLAY SS15",
                    description = "Premium esports arena with ultra-low latency setups.",
                    rating = 4.8,
                    reviewCount = 1200,
                    address = "Level 2, CyberHub Complex, SS15 Subang Jaya, Selangor, 47500.",
                    distanceText = "1.2 km away",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDhB3GL5Gi6HK6HsbWf3L3dflEOPCPx9tWWgySPBdDdB6bau-FIVc53mjv-vH5A8aCjAJfIKYGJe-UrH-QVTNRTKkAc-uTWhNjotfR0t2xb_5EHdz8tRtGVJfRHsfctNVJBNX4JWm_u0rbnfspdNbuscWROQmOY4TUcZcH9M_UI79uP_F5Ele44LwMiY01knNf0o0XLZ7WccWY-OAcQ1UiHw4_iAnyhdyYdo_jUjq2GVn2ccjAdVJLhR8VO6zZmbNGwBm3d9yHwPw",
                    startingPrice = 10.0,
                    featuresString = "Fiber 10Gbps, RTX 4090 Rigs, 240Hz OLED",
                    category = "PC"
                ),
                Studio(
                    id = 2,
                    name = "Nexus VR Lounge",
                    description = "Fully untethered room-scale VR experiences.",
                    rating = 4.9,
                    reviewCount = 850,
                    address = "Level 1, Nexus Tech Park, Sector 4, PJ.",
                    distanceText = "3.4 km away",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD2DUUPnuxwf0XGRXJCd6leabHcnivN860a2QjYmfmHIXLfmun7MEF73VUwFF1Oh1lE4HGQeTRJBwkNnGMJ7q6LBMeaG-EVYTvcVStta-TfWiSYgckgAtKddxKLkp3x2Bwb8SDadlgHo64ZlqABySXESKZdM5trbrAFywYiMqTxAMwjKqLeMVADs1YhOS0LUO99nBJFJXHVw4lWi9P-vTcIfIhumbEicOxd4WABlBO569dkeGn-eYyaneIX8S02Mu-iEWbVJbSSrA",
                    startingPrice = 25.0,
                    featuresString = "Meta Quest 3, 5x5m Room, Acoustic Treated",
                    category = "VR"
                ),
                Studio(
                    id = 3,
                    name = "Apex Racing Hub",
                    description = "Professional direct-drive sim rigs with motion.",
                    rating = 4.7,
                    reviewCount = 430,
                    address = "Ground Floor, Autodrome Plaza, Cyberjaya.",
                    distanceText = "15.2 km away",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB9MoJpicHDTmlbYF2ryMLhYrk8zC9ojr6bYxY-met06E3F8qeaZUPS7qoa8FXhV0aTzb3sWl8u4qRnSTJL2RWeGnHqHN_ql_s5ucFGiROozoeqrosQXMCvXna1xj8wV715qVo_SzovGnfEAee89vXtjTY1v1pHesT2yk-oNWanI9aRIYUVIO-xZLadkRyx3I_xhKTBmmZB7wLznfX5QQMmSrlRKN3ZakMj0gP0aInvT6ALn1oKFzZNMP3ZZ04dO5gAjTaVamLe9Q",
                    startingPrice = 40.0,
                    featuresString = "Direct Drive, Motion Rig, Triple Monitor",
                    category = "Racing"
                ),
                Studio(
                    id = 4,
                    name = "Zenith Gaming Lounge",
                    description = "Sleek and moody streaming setup with dynamic environments.",
                    rating = 4.8,
                    reviewCount = 310,
                    address = "Lot 44, Ground Floor, Damansara Uptown.",
                    distanceText = "8.3 km away",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAGmbS0MTAKMsChjfJfOHMxyv7BWbP-fNdvY_ANOuCf8X-jsHzIemfyGcA3LZx35U9XyZ5VG6dQdHhLm4QAaZ2t0Xg_pyulKt0qecks7NKOlh3ezPmbgXl5Jl_zyaAmVAuHZzYqpQOwmHvD0S5JNtDY6uPS6aRqC1MSj1ymOI3Uurpf93_I3JezX-St-i4-wxS0zQJd6AsNfEIMpBtEhLH3J_CWLgJ1HNxL3tb5YkOwXelX6lGBYZK2kkleiMeOsT9rBGl-oL-N3g",
                    startingPrice = 12.0,
                    featuresString = "240Hz OLED, Fiber Line, Cafe Inside",
                    category = "PS5"
                )
            )
            studioDao.insertStudios(studios)

            val stations = listOf(
                // Studio 1 (CTRL+PLAY SS15)
                Station(101, 1, "PC Rig #1", "PC", 15.00, "Available"),
                Station(102, 1, "PS5 Pod #2", "Console", 10.00, "Available"),
                Station(103, 1, "PC Rig #4", "PC", 15.00, "Booked"),
                Station(104, 1, "PS5 Pod #3", "Console", 10.00, "Available"),
                // Studio 2 (Nexus VR Lounge)
                Station(201, 2, "VR Pod #1", "VR", 25.00, "Available"),
                Station(202, 2, "VR Suite #2", "VR", 30.00, "Available"),
                // Studio 3 (Apex Racing Hub)
                Station(301, 3, "Sim Rig #1", "Racing", 40.00, "Available"),
                Station(302, 3, "Motion Rig #2", "Racing", 50.00, "Available"),
                // Studio 4 (Zenith Gaming Lounge)
                Station(401, 4, "PS5 Station #1", "Console", 12.00, "Available"),
                Station(402, 4, "PS5 Station #2", "Console", 12.00, "Available")
            )
            stationDao.insertStations(stations)

            val notifications = listOf(
                Notification(
                    title = "Studio Alpha Booking Approaching",
                    body = "Your streaming session in Pod 04 begins in 30 minutes. The rig is pre-configured to your saved profile.",
                    category = "SYSTEM ALERT",
                    timeText = "Just now"
                ),
                Notification(
                    title = "Level Up: Pro Status Achieved",
                    body = "Congratulations. You have surpassed 10,000 EXP. You now have access to priority booking and VIP lounge access.",
                    category = "OMNIELITE",
                    timeText = "2 hours ago"
                ),
                Notification(
                    title = "Double EXP Weekend Initiated",
                    body = "Book any VR rig this weekend and earn double OmniPoints toward your next hardware upgrade.",
                    category = "PROMOTION",
                    timeText = "Yesterday"
                )
            )
            for (notif in notifications) {
                notificationDao.insertNotification(notif)
            }
        }
    }
}
