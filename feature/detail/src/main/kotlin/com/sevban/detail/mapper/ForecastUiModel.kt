package com.sevban.detail.mapper

data class ForecastUiModel(
    val city: String,
    val eachDayWithAverage: List<EachDayWithAverage>,
    val today: List<com.sevban.ui.model.ForecastWeatherUi>,
)

data class EachDayWithAverage(
    val temperature: Int,
    val dayOfMonth: String,
    val dayOfWeek: String,
    val icon: String,
    val description: String
)