package com.sevban.data.mapper

import com.sevban.database.model.ForecastEntity
import com.sevban.database.model.ForecastWeatherEntity
import com.sevban.database.model.WeatherEntity
import com.sevban.network.source.remote.model.forecast.ForecastDTO
import com.sevban.network.source.remote.model.weather.WeatherDTO
import kotlin.math.round

/**
 * Generates a location-tolerant ID by rounding coordinates to 2 decimal places.
 * This ensures that nearby locations (within ~1km) share the same forecast data.
 */
private fun generateLocationId(latitude: Double, longitude: Double): String {
    val roundedLat = round(latitude * 100) / 100  // Round to 2 decimal places
    val roundedLng = round(longitude * 100) / 100
    return "${roundedLat}_${roundedLng}"
}

fun WeatherDTO.toEntity(latitude: Double, longitude: Double): WeatherEntity {
    return WeatherEntity(
        id = this.id ?: -1,
        description = this.weather?.firstOrNull()?.description,
        icon = this.weather?.firstOrNull()?.icon,
        cityName = this.name,
        feelsLike = this.main?.feelsLike,
        grndLevel = this.main?.grndLevel,
        humidity = this.main?.humidity,
        pressure = this.main?.pressure,
        seaLevel = this.main?.seaLevel,
        temp = this.main?.temp,
        tempMax = this.main?.tempMax,
        tempMin = this.main?.tempMin,
        visibility = this.visibility,
        windSpeed = this.wind?.speed,
        rainVolume1h = this.rain?.h,
        latitude = latitude,
        longitude = longitude,
        isSynced = true,
        lastUpdated = System.currentTimeMillis(),
        syncedAt = System.currentTimeMillis()
    )
}

fun ForecastDTO.toEntity(latitude: Double, longitude: Double): ForecastEntity {
    return ForecastEntity(
        id = generateLocationId(latitude, longitude),
        cod = this.cod,
        city = this.city?.name,
        cnt = this.cnt,
        message = this.message,
        latitude = latitude,
        longitude = longitude,
        isSynced = true,
        lastUpdated = System.currentTimeMillis(),
        syncedAt = System.currentTimeMillis()
    )
}

fun ForecastDTO.toWeatherEntities(forecastId: String): List<ForecastWeatherEntity> {
    return this.list?.map { networkForecast ->
        ForecastWeatherEntity(
            forecastId = forecastId,
            temperature = networkForecast.main?.temp,
            date = networkForecast.dtTxt ?: "",
            icon = networkForecast.weather?.firstOrNull()?.icon,
            description = networkForecast.weather?.firstOrNull()?.description,
            precipitationProbability = networkForecast.pop,
            rainVolume1h = networkForecast.rain?.h,
            isSynced = true,
            lastUpdated = System.currentTimeMillis(),
            syncedAt = System.currentTimeMillis()
        )
    } ?: emptyList()
} 