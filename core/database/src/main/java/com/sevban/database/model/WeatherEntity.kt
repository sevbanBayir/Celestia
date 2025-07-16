package com.sevban.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int,
    val description: String?,
    val icon: String?,
    val cityName: String?,
    val feelsLike: Double?,
    val grndLevel: Int?,
    val humidity: Int?,
    val pressure: Int?,
    val seaLevel: Int?,
    val temp: Double?,
    val tempMax: Double?,
    val tempMin: Double?,
    val visibility: Int?,
    val windSpeed: Double?,
    val rainVolume1h: Double?,
    val latitude: Double,
    val longitude: Double,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val syncedAt: Long? = null
) 