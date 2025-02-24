package com.sevban.home.mapper

import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toHourAndMinute
import com.sevban.common.extensions.toLocalDateTime
import com.sevban.common.extensions.toTitleCase
import com.sevban.model.Forecast
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.createWeatherIconURL
import java.time.LocalDateTime
import kotlin.math.roundToInt

data class ForecastUiModel(
    val city: String?,
    val forecastBy3Hours: List<ForecastWeatherUi>,
    val chartData: ChartData,
    val next24Hours: List<ForecastWeatherUi>
)

data class ChartData(
    val temperatures: List<Int>,
    val dateList: List<String>
)

fun Forecast.nex24Hours(): List<ForecastWeatherUi> {
    return temp.takeWhile {
        val now = LocalDateTime.now()
        val date = it.date.toLocalDateTime()
        date < now.plusHours(24)
    }.map {
        ForecastWeatherUi(
            temperature = it.temperature?.roundToInt() ?: 0,
            date = it.date.toHourAndMinute(),
            icon = createWeatherIconURL(it.icon),
            description = it.description?.toTitleCase() ?: String.EMPTY
        )
    }
}

fun Forecast.toForecastUiModel(): ForecastUiModel {

    val forecastBy3Hours = temp.map { forecastWeather ->
        ForecastWeatherUi(
            temperature = forecastWeather.temperature?.roundToInt() ?: 0,
            date = forecastWeather.date.toHourAndMinute(),
            icon = createWeatherIconURL(forecastWeather.icon ?: String.EMPTY),
            description = forecastWeather.description?.toTitleCase() ?: String.EMPTY
        )
    }

    val chartData = ChartData(
        temperatures = forecastBy3Hours.map { it.temperature }.take(8),
        dateList = forecastBy3Hours.map { it.date }.take(8)
    )

    return ForecastUiModel(
        city = city,
        forecastBy3Hours = forecastBy3Hours,
        chartData = chartData,
        next24Hours = nex24Hours()
    )
}