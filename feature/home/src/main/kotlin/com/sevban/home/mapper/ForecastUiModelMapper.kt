package com.sevban.home.mapper

import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toHourAndMinute
import com.sevban.common.extensions.toLocalDateTime
import com.sevban.common.extensions.toTitleCase
import com.sevban.common.helper.WeatherLocalizationService
import com.sevban.model.Forecast
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.createWeatherIconURL
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class ForecastUiModelMapper @Inject constructor(
    private val localizationService: WeatherLocalizationService
) {

    fun mapToUiModel(forecast: Forecast): ForecastUiModel {
        val forecastBy3Hours = forecast.temp.map { forecastWeather ->
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
            city = forecast.city,
            forecastBy3Hours = forecastBy3Hours,
            chartData = chartData,
            next24Hours = forecast.getNext24Hours(),
            todayHigh = forecast.getTodayHigh()?.toString() ?: "",
            todayLow = forecast.getTodayLow()?.toString() ?: "",
            precipitationChance = forecast.getHighestPrecipitationChance(),
            nextRainTime = forecast.getNextRainTime()
        )
    }

    private fun Forecast.getNext24Hours(): List<ForecastWeatherUi> {
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

    private fun Forecast.getTodayHigh(): Int? {
        return temp.take(8).maxOfOrNull { it.temperature?.toInt() ?: Int.MIN_VALUE }
            ?.takeIf { it != Int.MIN_VALUE }
    }

    private fun Forecast.getTodayLow(): Int? {
        return temp.take(8).minOfOrNull { it.temperature?.toInt() ?: Int.MAX_VALUE }
            ?.takeIf { it != Int.MAX_VALUE }
    }

    private fun Forecast.getHighestPrecipitationChance(): String {
        val maxProbability = temp.take(8).maxOfOrNull { it.precipitationProbability ?: 0.0 }
        return localizationService.formatPrecipitationChance(maxProbability)
    }

    private fun Forecast.getNextRainTime(): String {
        val nextRain = temp.find {
            it.precipitationProbability != null && it.precipitationProbability!! > 0.3
        }
        return if (nextRain != null) {
            val time = nextRain.date.toLocalDateTime()
            val now = LocalDateTime.now()
            val hoursUntilRain = java.time.Duration.between(now, time).toHours()
            if (hoursUntilRain < 24) {
                nextRain.date.toHourAndMinute()
            } else {
                ""
            }
        } else {
            ""
        }
    }
} 