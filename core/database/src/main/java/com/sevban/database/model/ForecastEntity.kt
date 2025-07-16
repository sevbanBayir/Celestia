package com.sevban.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey val id: String, // Composite key using lat_long
    val cod: String?,
    val city: String?,
    val cnt: Int?,
    val message: Int?,
    val latitude: Double,
    val longitude: Double,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val syncedAt: Long? = null
) 