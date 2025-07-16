package com.sevban.data.repository

import com.sevban.common.model.Failure
import com.sevban.model.Forecast
import com.sevban.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherForecastRepository {
    fun getLocationWeather(
        lat: String,
        long: String
    ): Flow<Weather>

    fun getLocationForecast(
        lat: String,
        long: String
    ): Flow<Forecast>
    
    /**
     * Force refresh weather data from network regardless of cache state.
     * Use this for user-initiated refresh (pull-to-refresh).
     * @throws Failure with CONNECTIVITY_ERROR if no internet connection
     */
    suspend fun forceRefreshWeather(lat: String, long: String)
    
    /**
     * Force refresh forecast data from network regardless of cache state.
     * Use this for user-initiated refresh (pull-to-refresh).
     * @throws Failure with CONNECTIVITY_ERROR if no internet connection
     */
    suspend fun forceRefreshForecast(lat: String, long: String)
}