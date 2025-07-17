package com.sevban.ui.mapper

import com.sevban.common.constants.Constants
import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toTitleCase
import com.sevban.common.helper.WeatherLocalizationService
import com.sevban.common.helper.getVideoName
import com.sevban.common.helper.toWeatherType
import com.sevban.model.Weather
import com.sevban.ui.model.WeatherUiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherUiModelMapper @Inject constructor(
    private val localizationService: WeatherLocalizationService
) {

    fun mapToUiModel(weather: Weather): WeatherUiModel {
        return WeatherUiModel(
            id = weather.id,
            description = weather.description?.toTitleCase() ?: String.EMPTY,
            iconUrl = createWeatherIconURL(weather.icon),
            cityName = weather.cityName ?: String.EMPTY,
            feelsLike = weather.feelsLike?.toInt()?.toString() ?: String.EMPTY,
            grndLevel = weather.grndLevel?.toString() ?: String.EMPTY,
            humidity = weather.humidity?.toString() ?: String.EMPTY,
            pressure = weather.pressure?.toString() ?: String.EMPTY,
            seaLevel = weather.seaLevel?.toString() ?: String.EMPTY,
            temp = weather.temp?.toInt()?.toString() ?: String.EMPTY,
            tempMax = weather.tempMax?.toInt()?.toString() ?: String.EMPTY,
            tempMin = weather.tempMin?.toInt()?.toString() ?: String.EMPTY,
            visibility = weather.visibility?.toString() ?: String.EMPTY,
            windSpeed = weather.windSpeed?.toString() ?: String.EMPTY,
            video = weather.description?.toWeatherType()?.getVideoName(),
            // Localized fields
            pressureStatus = localizationService.getPressureStatus(weather.pressure?.toInt()),
            pressureWithUnit = localizationService.formatPressureWithUnit(weather.pressure?.toInt()),
            tempRange = localizationService.formatTempRange(weather.tempMin, weather.tempMax),
            rainInfo = localizationService.formatRainInfo(weather.rainVolume1h),
            isRaining = localizationService.isRaining(weather.rainVolume1h),
            lastUpdated = weather.lastUpdated
        )
    }

    private fun createWeatherIconURL(icon: String?): String {
        require(!icon.isNullOrEmpty()) { "Icon cannot be null or empty" }
        return Constants.WEATHER_IMAGE_BASE_URL + icon + Constants.IMAGE_RESOLUTION
    }
} 