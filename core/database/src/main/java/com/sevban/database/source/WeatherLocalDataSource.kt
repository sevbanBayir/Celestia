package com.sevban.database.source

import com.sevban.database.model.ForecastWithWeatherItems
import com.sevban.database.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    
    // Weather operations
    fun getWeatherByLocation(lat: Double, lng: Double): Flow<WeatherEntity?>
    suspend fun insertWeather(weather: WeatherEntity)
    suspend fun getUnsyncedWeather(): List<WeatherEntity>
    suspend fun markWeatherAsSynced(id: Int)
    suspend fun deleteOldWeatherData(olderThan: Long)
    
    // Forecast operations
    fun getForecastByLocation(lat: Double, lng: Double): Flow<ForecastWithWeatherItems?>
    suspend fun insertForecastWithWeatherItems(forecast: ForecastWithWeatherItems)
    suspend fun getUnsyncedForecasts(): List<ForecastWithWeatherItems>
    suspend fun markForecastAsSynced(id: String)
    suspend fun deleteOldForecastData(olderThan: Long)
} 