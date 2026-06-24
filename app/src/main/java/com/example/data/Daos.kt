package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudioDao {
    @Query("SELECT * FROM studios")
    fun getAllStudios(): Flow<List<Studio>>

    @Query("SELECT * FROM studios WHERE id = :id LIMIT 1")
    suspend fun getStudioById(id: Int): Studio?

    @Query("SELECT * FROM studios WHERE category = :category")
    fun getStudiosByCategory(category: String): Flow<List<Studio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudios(studios: List<Studio>)
}

@Dao
interface StationDao {
    @Query("SELECT * FROM stations WHERE studioId = :studioId")
    fun getStationsForStudio(studioId: Int): Flow<List<Station>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<Station>)

    @Update
    suspend fun updateStation(station: Station)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking): Long

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBookingById(id: Int)

    @Update
    suspend fun updateBooking(booking: Booking)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}
