package com.sevban.data.repository

import com.sevban.common.helper.DispatcherProvider
import com.sevban.common.model.ErrorType
import com.sevban.common.model.Failure
import com.sevban.data.mapper.toForecast
import com.sevban.data.mapper.toWeather
import com.sevban.model.Forecast
import com.sevban.model.Weather
import com.sevban.network.source.remote.model.forecast.ForecastDTO
import com.sevban.network.source.remote.model.weather.WeatherDTO
import com.sevban.network.source.remote.WeatherRemoteDataSource
import com.sevban.network.util.asRestApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherForecastRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider
) : WeatherForecastRepository {
    override fun getLocationWeather(lat: String, long: String): Flow<Weather> =
        weatherRemoteDataSource.getLocationWeather(lat, long)
            .asRestApiCall(WeatherDTO::toWeather)
            .catch {
                if (it is UnknownHostException) {
                    throw Failure(ErrorType.CONNECTIVITY_ERROR)
                } else throw it
            }
            .flowOn(dispatcherProvider.ioDispatcher)


    override fun getLocationForecast(lat: String, long: String): Flow<Forecast> =
        weatherRemoteDataSource.getLocationForecast(lat, long)
            .asRestApiCall(ForecastDTO::toForecast)
            .flowOn(dispatcherProvider.ioDispatcher)

}