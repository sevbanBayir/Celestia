package com.sevban.ui.model

import com.sevban.common.constants.Constants
import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toTitleCase
import com.sevban.common.helper.getVideoName
import com.sevban.common.helper.toWeatherType
import com.sevban.model.Weather

data class WeatherUiModel(
    val id: Int,
    val description: String,
    val iconUrl: String,
    val cityName: String,
    val feelsLike: String,
    val grndLevel: String,
    val humidity: String,
    val pressure: String,
    val seaLevel: String,
    val temp: String,
    val tempMax: String,
    val tempMin: String,
    val visibility: String,
    val windSpeed: String,
    val video: String?,
    // New localized fields
    val pressureStatus: String,
    val pressureWithUnit: String,
    val tempRange: String,
    val rainInfo: String, // Formatted rain information
    val isRaining: Boolean
)

fun Weather.toWeatherUiModel() = WeatherUiModel(
    id = id,
    description = description?.toTitleCase() ?: String.EMPTY,
    iconUrl = createWeatherIconURL(icon),
    cityName = cityName ?: String.EMPTY,
    feelsLike = feelsLike?.toInt().toString(),
    grndLevel = grndLevel?.toString() ?: String.EMPTY,
    humidity = humidity?.toString() ?: String.EMPTY,
    pressure = pressure?.toString() ?: String.EMPTY,
    seaLevel = seaLevel?.toString() ?: String.EMPTY,
    temp = temp?.toInt()?.toString() ?: String.EMPTY,
    tempMax = tempMax?.toString() ?: String.EMPTY,
    tempMin = tempMin?.toString() ?: String.EMPTY,
    visibility = visibility?.toString() ?: String.EMPTY,
    windSpeed = windSpeed?.toString() ?: String.EMPTY,
    video = description?.toWeatherType()?.getVideoName(),
    // Default values for new fields (used by tests)
    pressureStatus = "",
    pressureWithUnit = pressure?.toString() ?: "",
    tempRange = "",
    rainInfo = "",
    isRaining = false
)

fun createWeatherIconURL(icon: String?): String {
    require(!icon.isNullOrEmpty()) { "Icon cannot be null or empty" }
    return Constants.WEATHER_IMAGE_BASE_URL + icon + Constants.IMAGE_RESOLUTION
}
