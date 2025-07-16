package com.sevban.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sevban.database.model.ForecastEntity
import com.sevban.database.model.ForecastWeatherEntity
import com.sevban.database.model.ForecastWithWeatherItems
import com.sevban.database.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    
    // Weather operations
    @Query("SELECT * FROM weather WHERE latitude = :lat AND longitude = :lng")
    fun getWeatherByLocation(lat: Double, lng: Double): Flow<WeatherEntity?>
    
    @Query("SELECT * FROM weather WHERE isSynced = 0")
    suspend fun getUnsyncedWeather(): List<WeatherEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)
    
    @Query("UPDATE weather SET isSynced = 1, syncedAt = :syncedAt WHERE id = :id")
    suspend fun markWeatherAsSynced(id: Int, syncedAt: Long)
    
    @Query("DELETE FROM weather WHERE lastUpdated < :timestamp")
    suspend fun deleteOldWeatherData(timestamp: Long)
    
    // Forecast operations
    @Query("SELECT * FROM forecast WHERE latitude = :lat AND longitude = :lng")
    fun getForecastByLocation(lat: Double, lng: Double): Flow<ForecastEntity?>
    
    @Transaction
    @Query("SELECT * FROM forecast WHERE latitude = :lat AND longitude = :lng")
    fun getForecastWithWeatherItems(lat: Double, lng: Double): Flow<ForecastWithWeatherItems?>
    
    @Query("SELECT * FROM forecast WHERE isSynced = 0")
    suspend fun getUnsyncedForecasts(): List<ForecastEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: ForecastEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastWeatherItems(items: List<ForecastWeatherEntity>)
    
    @Transaction
    suspend fun insertForecastWithWeatherItems(
        forecast: ForecastEntity,
        weatherItems: List<ForecastWeatherEntity>
    ) {
        insertForecast(forecast)
        insertForecastWeatherItems(weatherItems)
    }
    
    @Query("UPDATE forecast SET isSynced = 1, syncedAt = :syncedAt WHERE id = :id")
    suspend fun markForecastAsSynced(id: String, syncedAt: Long)
    
    @Query("DELETE FROM forecast WHERE lastUpdated < :timestamp")
    suspend fun deleteOldForecastData(timestamp: Long)
    
    @Query("DELETE FROM forecast_weather WHERE forecastId = :forecastId")
    suspend fun deleteForecastWeatherItems(forecastId: String)
}

