package com.sevban.model

data class ForecastWeather(
    val temperature: Double?,
    val date: String,
    val icon: String?,
    val description: String?,
    val precipitationProbability: Double?, // Probability of precipitation (0.0 to 1.0)
    val rainVolume1h: Double? // Rain volume in 1 hour (mm)
)