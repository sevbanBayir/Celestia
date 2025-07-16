package com.sevban.network.source.remote.model.weather


import com.google.gson.annotations.SerializedName

data class NetworkWeather(
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val name: String?
)