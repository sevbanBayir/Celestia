package com.sevban.home.components.videobg

import com.sevban.common.helper.isDaylight

enum class WeatherType {
    SUNNY,
    CLOUDY,
    RAINY,
    SNOWY,
    WINDY,
    FOGGY,
    THUNDERSTORM,
    UNKNOWN,
}

fun String.toWeatherType(): WeatherType {
    val weatherKeywords = mapOf(
        WeatherType.SUNNY to listOf("clear", "sun"),
        WeatherType.CLOUDY to listOf("cloud", "overcast"),
        WeatherType.RAINY to listOf("rain", "drizzle", "shower"),
        WeatherType.SNOWY to listOf("snow", "sleet"),
        WeatherType.WINDY to listOf("wind", "breeze", "gale"),
        WeatherType.FOGGY to listOf("fog", "mist", "haze"),
        WeatherType.THUNDERSTORM to listOf("thunder")
    )

    return weatherKeywords.entries.firstOrNull { (_, keywords) ->
        keywords.any { keyword -> this.contains(keyword, ignoreCase = true) }
    }?.key ?: WeatherType.UNKNOWN
}

fun WeatherType.getVideoName(): String? {
    return when (this) {
        WeatherType.SNOWY -> "snowy"
        WeatherType.WINDY -> "windy"
        WeatherType.RAINY -> "rainy"
        WeatherType.SUNNY -> if (isDaylight()) "sunny" else "sunny_evening"
        WeatherType.CLOUDY -> "cloudy"
        WeatherType.FOGGY -> "foggy"
        WeatherType.THUNDERSTORM -> "thunderstorm"
        WeatherType.UNKNOWN -> null
    }
}