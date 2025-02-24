package com.sevban.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.model.Failure
import com.sevban.detail.mapper.toForecastUiModel
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.model.Forecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _retryTrigger = Channel<Unit>()
    val retryTrigger = _retryTrigger.receiveAsFlow()

    val forecastState = retryTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            combine(
                savedStateHandle.getStateFlow<Double?>(LATITUDE_ARG, null),
                savedStateHandle.getStateFlow<Double?>(LONGITUDE_ARG, null)
            ) { lat, long ->
                if (lat == null || long == null) { return@combine null }
                lat to long
            }.filterNotNull().flatMapLatest { (latitude, longitude) ->
                getForecastUseCase.execute(
                    lat = latitude.toString(),
                    long = longitude.toString()
                )
            }.map<Forecast, ForecastState> {
                ForecastState.Success(it.toForecastUiModel())
            }.catch {
                val failure = it as? Failure ?: Failure(throwable = it)
                emit(ForecastState.Error(failure))
            }.onStart { emit(ForecastState.Loading) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ForecastState.Loading
        )

    fun onEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.OnTryAgainClick -> {
                viewModelScope.launch {
                    _retryTrigger.send(Unit)
                }
            }
        }
    }

    companion object {
        const val LATITUDE_ARG = "latitude"
        const val LONGITUDE_ARG = "longitude"
    }
}