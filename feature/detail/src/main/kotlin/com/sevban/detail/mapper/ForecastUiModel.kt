package com.sevban.detail.mapper

import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toHourAndMinute
import com.sevban.common.extensions.toLocalDate
import com.sevban.common.extensions.toTitleCase
import com.sevban.common.helper.dayOfMonthAndName
import com.sevban.common.helper.isToday
import com.sevban.model.Forecast
import com.sevban.model.ForecastWeather
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.createWeatherIconURL
import kotlin.math.roundToInt

data class ForecastUiModel(
    val city: String,
    val eachDayWithAverage: List<EachDayWithAverage>,
    val today: List<ForecastWeatherUi>,
)

data class EachDayWithAverage(
    val temperature: Int,
    val dayOfMonth: String,
    val dayOfWeek: String,
    val icon: String,
    val description: String
)

fun Forecast.toForecastUiModel(): ForecastUiModel {
    val tempGroupedByDay = temp
        .dropWhile { it.date.toLocalDate().isToday() }
        .groupBy { it.date.toLocalDate() }

    val otherDaysForecastMap = tempGroupedByDay.map {
        val weatherList = it.value
        val average = weatherList.mapNotNull(ForecastWeather::temperature).average()
        val dayOfMonthAndName = it.key.dayOfMonthAndName.lowercase()

        EachDayWithAverage(
            temperature = average.roundToInt(),
            dayOfMonth = dayOfMonthAndName.toTitleCase(),
            dayOfWeek = it.key.dayOfWeek.name,
            icon = createWeatherIconURL(weatherList.first().icon),
            description = weatherList.first().description?.toTitleCase() ?: String.EMPTY
        )
    }

    val today = temp.takeWhile { it.date.toLocalDate().isToday() }.map {
        ForecastWeatherUi(
            date = it.date.toHourAndMinute(),
            icon = createWeatherIconURL(it.icon ?: String.EMPTY),
            description = it.description?.toTitleCase() ?: String.EMPTY,
            temperature = it.temperature?.roundToInt() ?: 0
        )
    }

    return ForecastUiModel(
        city = city ?: String.EMPTY,
        eachDayWithAverage = otherDaysForecastMap,
        today = today
    )
}