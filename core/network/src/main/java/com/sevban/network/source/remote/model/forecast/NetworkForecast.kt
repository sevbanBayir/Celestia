package com.sevban.network.source.remote.model.forecast


import com.google.gson.annotations.SerializedName
import com.sevban.network.source.remote.model.weather.Clouds
import com.sevban.network.source.remote.model.weather.Main
import com.sevban.network.source.remote.model.weather.NetworkWeather
import com.sevban.network.source.remote.model.weather.Rain
import com.sevban.network.source.remote.model.weather.Sys
import com.sevban.network.source.remote.model.weather.Wind

data class NetworkForecast(
    @SerializedName("clouds")
    val clouds: Clouds?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("dt_txt")
    val dtTxt: String?,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("pop")
    val pop: Double?,
    @SerializedName("rain")
    val rain: Rain?,
    @SerializedName("sys")
    val sys: Sys?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weather: List<NetworkWeather?>?,
    @SerializedName("wind")
    val wind: Wind?
)