package com.sevban.data.repository

import com.sevban.common.di.RepositoryScope
import com.sevban.common.helper.DispatcherProvider
import com.sevban.common.model.ErrorType
import com.sevban.common.model.Failure
import com.sevban.data.mapper.toEntity
import com.sevban.data.mapper.toDomain
import com.sevban.data.mapper.toWeatherEntities
import com.sevban.database.model.ForecastWithWeatherItems
import com.sevban.database.source.WeatherLocalDataSource
import com.sevban.model.Forecast
import com.sevban.model.Weather
import com.sevban.network.source.remote.WeatherRemoteDataSource
import com.sevban.network.util.asRestApiCallWithDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherForecastRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val dispatcherProvider: DispatcherProvider,
    @RepositoryScope private val repositoryScope: CoroutineScope
) : WeatherForecastRepository {

    companion object {
        private const val DATA_STALE_THRESHOLD_MS = 30 * 60 * 1000L // 30 minutes
    }

    override fun getLocationWeather(lat: String, long: String): Flow<Weather> {
        val latitude = lat.toDouble()
        val longitude = long.toDouble()
        
        return weatherLocalDataSource.getWeatherByLocation(latitude, longitude)
            .mapNotNull { entity ->
                val weather = entity?.toDomain()
                if (weather == null) fetchWeatherFromNetwork(latitude, longitude)
                weather
            }
            .flowOn(dispatcherProvider.ioDispatcher)
    }

    override fun getLocationForecast(lat: String, long: String): Flow<Forecast> {
        val latitude = lat.toDouble()
        val longitude = long.toDouble()
        
        return weatherLocalDataSource.getForecastByLocation(latitude, longitude)
            .mapNotNull { forecastWithWeatherItems ->
                val forecast = forecastWithWeatherItems?.toDomain()
                if (forecast == null) fetchForecastFromNetwork(latitude, longitude)
                forecast
            }
            .flowOn(dispatcherProvider.ioDispatcher)
    }

    private fun refreshWeatherIfNeeded(latitude: Double, longitude: Double) {
        repositoryScope.launch {
            try {
                val existingWeather = weatherLocalDataSource.getWeatherByLocation(latitude, longitude).first()
                if (existingWeather == null || isDataStale(existingWeather.lastUpdated)) {
                    fetchWeatherFromNetwork(latitude, longitude)
                }
            } catch (e: Exception) {
                // Log error but don't crash - we're in background
                // In production, you might want to use a proper logging framework
            }
        }
    }

    private fun refreshForecastIfNeeded(latitude: Double, longitude: Double) {
        repositoryScope.launch {
            try {
                val existingForecast = weatherLocalDataSource.getForecastByLocation(latitude, longitude).first()
                if (existingForecast == null || isDataStale(existingForecast.forecast.lastUpdated)) {
                    fetchForecastFromNetwork(latitude, longitude)
                }
            } catch (e: Exception) {
                // Log error but don't crash - we're in background
            }
        }
    }

    private suspend fun fetchWeatherFromNetwork(latitude: Double, longitude: Double) {
        try {
            weatherRemoteDataSource.getLocationWeather(latitude.toString(), longitude.toString())
                .asRestApiCallWithDto()
                .catch { exception ->
                    // Only swallow connectivity errors in background refresh
                    // Other errors should be propagated
                    if (exception is UnknownHostException) {
                        // Network connectivity issue - this is expected in offline mode
                        return@catch
                    } else {
                        throw exception
                    }
                }
                .collect { weatherDTO ->
                    // Convert and save to database
                    val weatherEntity = weatherDTO.toEntity(latitude, longitude)
                    weatherLocalDataSource.insertWeather(weatherEntity)
                }
        } catch (e: Exception) {
            // In offline-first mode, network failures are non-critical for background refresh
            // Database will provide cached data if available
            when (e) {
                is UnknownHostException -> {
                    // Network connectivity issue - expected in offline mode
                }
                else -> {
                    // Other errors might be worth logging
                    // In production, you might want to use a proper logging framework
                }
            }
        }
    }

    private suspend fun fetchForecastFromNetwork(latitude: Double, longitude: Double) {
        try {
            weatherRemoteDataSource.getLocationForecast(latitude.toString(), longitude.toString())
                .asRestApiCallWithDto()
                .catch { exception ->
                    // Only swallow connectivity errors in background refresh
                    // Other errors should be propagated
                    if (exception is UnknownHostException) {
                        // Network connectivity issue - this is expected in offline mode
                        return@catch
                    } else {
                        throw exception
                    }
                }
                .collect { forecastDTO ->
                    // Convert and save to database
                    val forecastEntity = forecastDTO.toEntity(latitude, longitude)
                    val forecastWeatherEntities = forecastDTO.toWeatherEntities(forecastEntity.id)
                    
                    val forecastWithWeatherItems = ForecastWithWeatherItems(
                        forecast = forecastEntity,
                        weatherItems = forecastWeatherEntities
                    )
                    
                    weatherLocalDataSource.insertForecastWithWeatherItems(forecastWithWeatherItems)
                }
        } catch (e: Exception) {
            // In offline-first mode, network failures are non-critical for background refresh
            // Database will provide cached data if available
            when (e) {
                is UnknownHostException -> {
                    // Network connectivity issue - expected in offline mode
                }
                else -> {
                    // Other errors might be worth logging
                    // In production, you might want to use a proper logging framework
                }
            }
        }
    }

    private fun isDataStale(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated > DATA_STALE_THRESHOLD_MS
    }
    
    /**
     * Force refresh data from network regardless of cache state.
     * Use this for user-initiated refresh (pull-to-refresh).
     */
    override suspend fun forceRefreshWeather(lat: String, long: String) {
        val latitude = lat.toDouble()
        val longitude = long.toDouble()
        
        try {
            fetchWeatherFromNetwork(latitude, longitude)
        } catch (e: UnknownHostException) {
            throw Failure(ErrorType.CONNECTIVITY_ERROR)
        }
    }
    
    /**
     * Force refresh forecast from network regardless of cache state.
     * Use this for user-initiated refresh (pull-to-refresh).
     */
    override suspend fun forceRefreshForecast(lat: String, long: String) {
        val latitude = lat.toDouble()
        val longitude = long.toDouble()
        
        try {
            fetchForecastFromNetwork(latitude, longitude)
        } catch (e: UnknownHostException) {
            throw Failure(ErrorType.CONNECTIVITY_ERROR)
        }
    }
}