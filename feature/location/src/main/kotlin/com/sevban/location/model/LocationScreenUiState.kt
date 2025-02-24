package com.sevban.location.model

import com.sevban.model.Place
import com.sevban.ui.model.WeatherUiModel

data class LocationScreenUiState(
    val selectedPlace: Place? = null,
    val weather: WeatherUiModel? = null,
    val isPlaceListLoading: Boolean = false,
)
