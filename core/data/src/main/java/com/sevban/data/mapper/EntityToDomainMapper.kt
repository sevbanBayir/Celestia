package com.sevban.data.mapper

import com.sevban.database.model.ForecastWithWeatherItems
import com.sevban.database.model.ForecastWeatherEntity
import com.sevban.database.model.WeatherEntity
import com.sevban.model.Forecast
import com.sevban.model.ForecastWeather
import com.sevban.model.Weather

fun WeatherEntity.toDomain() = Weather(
    id = this.id,
    description = this.description,
    icon = this.icon,
    cityName = this.cityName,
    feelsLike = this.feelsLike,
    grndLevel = this.grndLevel,
    humidity = this.humidity,
    pressure = this.pressure,
    seaLevel = this.seaLevel,
    temp = this.temp,
    tempMax = this.tempMax,
    tempMin = this.tempMin,
    visibility = this.visibility,
    windSpeed = this.windSpeed,
    rainVolume1h = this.rainVolume1h,
    lastUpdated = this.lastUpdated
)

fun ForecastWithWeatherItems.toDomain() = Forecast(
    cod = this.forecast.cod,
    city = this.forecast.city,
    cnt = this.forecast.cnt,
    message = this.forecast.message,
    temp = this.weatherItems.map { it.toDomain() },
    lastUpdated = this.forecast.lastUpdated
)

fun ForecastWeatherEntity.toDomain() = ForecastWeather(
    temperature = this.temperature,
    date = this.date,
    icon = this.icon,
    description = this.description,
    precipitationProbability = this.precipitationProbability,
    rainVolume1h = this.rainVolume1h
) 