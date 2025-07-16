package com.sevban.network.source.remote.model.forecast


import com.google.gson.annotations.SerializedName

data class ForecastDTO(
    @SerializedName("city")
    val city: City?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("list")
    val list: List<NetworkForecast>?,
    @SerializedName("message")
    val message: Int?
)