package com.sevban.network.source.remote

import com.sevban.network.source.remote.model.forecast.ForecastDTO
import com.sevban.network.source.remote.model.weather.WeatherDTO
import com.sevban.network.util.Constants.METRIC
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("data/2.5/weather")
    suspend fun getLocationWeather(
        @Query("units") units: String = METRIC,
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("lang") lang: String
    ): Response<WeatherDTO>

    @GET("data/2.5/forecast")
    suspend fun getLocationForecast(
        @Query("units") units: String = METRIC,
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("lang") lang: String
    ): Response<ForecastDTO>
}
