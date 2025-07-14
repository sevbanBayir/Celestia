package com.sevban.detail.mapper

import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toHourAndMinute
import com.sevban.common.extensions.toLocalDate
import com.sevban.common.extensions.toTitleCase
import com.sevban.common.helper.WeatherLocalizationService
import com.sevban.common.helper.isToday
import com.sevban.model.Forecast
import com.sevban.model.ForecastWeather
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.createWeatherIconURL
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class DetailForecastUiModelMapper @Inject constructor(
    private val localizationService: WeatherLocalizationService
) {

    fun mapToUiModel(forecast: Forecast): ForecastUiModel {
        val tempGroupedByDay = forecast.temp
            .dropWhile { it.date.toLocalDate().isToday() }
            .groupBy { it.date.toLocalDate() }

        val otherDaysForecastMap = tempGroupedByDay.map { (date, weatherList) ->
            val average = weatherList.mapNotNull(ForecastWeather::temperature).average()
            val localizedDayAndMonth = localizationService.getLocalizedDayAndMonth(date)
            val localizedDayOfWeek = localizationService.getLocalizedDayOfWeek(date.dayOfWeek)

            EachDayWithAverage(
                temperature = average.roundToInt(),
                dayOfMonth = localizedDayAndMonth,
                dayOfWeek = localizedDayOfWeek,
                icon = createWeatherIconURL(weatherList.first().icon),
                description = weatherList.first().description?.toTitleCase() ?: String.EMPTY
            )
        }

        val today = forecast.temp.takeWhile { it.date.toLocalDate().isToday() }.map {
            ForecastWeatherUi(
                date = it.date.toHourAndMinute(),
                icon = createWeatherIconURL(it.icon ?: String.EMPTY),
                description = it.description?.toTitleCase() ?: String.EMPTY,
                temperature = it.temperature?.roundToInt() ?: 0
            )
        }

        return ForecastUiModel(
            city = forecast.city ?: String.EMPTY,
            eachDayWithAverage = otherDaysForecastMap,
            today = today
        )
    }
} 