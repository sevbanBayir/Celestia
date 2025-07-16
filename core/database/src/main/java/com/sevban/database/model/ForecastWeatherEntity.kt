package com.sevban.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "forecast_weather",
    foreignKeys = [
        ForeignKey(
            entity = ForecastEntity::class,
            parentColumns = ["id"],
            childColumns = ["forecastId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ForecastWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val forecastId: String,
    val temperature: Double?,
    val date: String,
    val icon: String?,
    val description: String?,
    val precipitationProbability: Double?,
    val rainVolume1h: Double?,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val syncedAt: Long? = null
) 