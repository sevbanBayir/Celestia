package com.sevban.data.mapper

import com.sevban.model.Forecast
import com.sevban.model.ForecastWeather
import com.sevban.network.source.remote.model.forecast.ForecastDTO

fun ForecastDTO.toForecast() = Forecast(
    cod = this.cod,
    city = this.city?.name,
    cnt = this.cnt,
    message = this.message,
    temp = this.list?.map {
        ForecastWeather(
            temperature = it.main?.temp,
            date = it.dtTxt ?: "",
            icon = it.weather?.firstOrNull()?.icon,
            description = it.weather?.firstOrNull()?.description,
            precipitationProbability = it.pop,
            rainVolume1h = it.rain?.h
        )
    } ?: emptyList(),
)