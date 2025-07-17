package com.sevban.domain.usecase

import com.sevban.data.repository.WeatherForecastRepository
import com.sevban.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherForecastRepository,
) {
    fun execute(
        lat: String,
        long: String,
        fromNetwork: Boolean = false
    ): Flow<Weather> = repository.getLocationWeather(lat, long)
        .onStart { if (fromNetwork) repository.forceRefreshWeather(lat, long) }
}