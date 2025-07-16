package com.sevban.model

data class Forecast(
    val cod: String?,
    val city: String?,
    val cnt: Int?,
    val message: Int?,
    val temp: List<ForecastWeather>,
    val lastUpdated: Long = System.currentTimeMillis() // Timestamp when this data was last updated
)