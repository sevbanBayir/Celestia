package com.sevban.home.model

import com.sevban.common.extensions.EMPTY
import com.sevban.common.model.Failure
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.ui.model.LocationArgument
import com.sevban.ui.model.WeatherUiModel

data class WeatherScreenUiState(
    val shouldShowPermanentlyDeclinedDialog: Boolean = false,
    val isPermissionDeclined: Boolean = false,
    val isPermissionPermanentlyDeclined: Boolean = false,
    val lastFetchedTime: String = String.EMPTY,
    val location : LocationArgument? = null
)

sealed interface WeatherState {
    data object Loading : WeatherState
    data object NoLocationPermission : WeatherState
    data class Error(val failure: Failure) : WeatherState
    data class Success(val weather: WeatherUiModel, val forecast: ForecastUiModel) : WeatherState
}