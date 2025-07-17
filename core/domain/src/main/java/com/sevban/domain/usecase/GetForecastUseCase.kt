package com.sevban.domain.usecase

import com.sevban.data.repository.WeatherForecastRepository
import com.sevban.model.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherForecastRepository
) {
    fun execute(
        lat: String,
        long: String,
        fromNetwork: Boolean = false
    ): Flow<Forecast> = repository.getLocationForecast(lat, long)
        .onStart { if (fromNetwork) repository.forceRefreshForecast(lat, long) }
}
