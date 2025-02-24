package com.sevban.detail

import com.sevban.common.model.Failure
import com.sevban.detail.mapper.ForecastUiModel

sealed interface ForecastState {
    data object Loading : ForecastState
    data class Error(val failure: Failure) : ForecastState
    data class Success(val forecast: ForecastUiModel) : ForecastState
}