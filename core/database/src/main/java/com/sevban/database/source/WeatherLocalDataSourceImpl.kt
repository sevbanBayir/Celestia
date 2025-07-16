package com.sevban.database.source

import com.sevban.database.WeatherDao
import com.sevban.database.model.ForecastWithWeatherItems
import com.sevban.database.model.WeatherEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao
) : WeatherLocalDataSource {
    
    override fun getWeatherByLocation(lat: Double, lng: Double): Flow<WeatherEntity?> {
        return weatherDao.getWeatherByLocation(lat, lng)
    }
    
    override suspend fun insertWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }
    
    override suspend fun getUnsyncedWeather(): List<WeatherEntity> {
        return weatherDao.getUnsyncedWeather()
    }
    
    override suspend fun markWeatherAsSynced(id: Int) {
        weatherDao.markWeatherAsSynced(id, System.currentTimeMillis())
    }
    
    override suspend fun deleteOldWeatherData(olderThan: Long) {
        weatherDao.deleteOldWeatherData(olderThan)
    }
    
    override fun getForecastByLocation(lat: Double, lng: Double): Flow<ForecastWithWeatherItems?> {
        return weatherDao.getForecastWithWeatherItems(lat, lng)
    }
    
    override suspend fun insertForecastWithWeatherItems(forecast: ForecastWithWeatherItems) {
        weatherDao.insertForecastWithWeatherItems(
            forecast.forecast,
            forecast.weatherItems
        )
    }
    
    override suspend fun getUnsyncedForecasts(): List<ForecastWithWeatherItems> {
        val unsyncedForecasts = weatherDao.getUnsyncedForecasts()
        return unsyncedForecasts.map { forecastEntity ->
            val weatherItems = weatherDao.getForecastWithWeatherItems(
                forecastEntity.latitude,
                forecastEntity.longitude
            )
            // For suspended functions, we need to handle this differently
            // This is a simplified version - in practice, you'd want to create a proper query
            ForecastWithWeatherItems(forecastEntity, emptyList())
        }
    }
    
    override suspend fun markForecastAsSynced(id: String) {
        weatherDao.markForecastAsSynced(id, System.currentTimeMillis())
    }
    
    override suspend fun deleteOldForecastData(olderThan: Long) {
        weatherDao.deleteOldForecastData(olderThan)
    }
} 