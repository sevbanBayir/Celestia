package com.sevban.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.location.LocationObserver
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.common.model.Failure
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.home.mapper.toForecastUiModel
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.ui.model.toWeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    locationObserver: LocationObserver,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val retryTrigger = Channel<Unit>()

    val weatherState = retryTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .flatMapLatest {
            combine(
                locationObserver.getCurrentLocation(),
                savedStateHandle.getStateFlow<Double?>(LATITUDE_ARG, null),
                savedStateHandle.getStateFlow<Double?>(LONGITUDE_ARG, null)
            ) { location, lat, long ->
                val (latitude, longitude) = if (lat != null && long != null) {
                    lat to long
                } else {
                    location.latitude to location.longitude
                }
                _uiState.update {
                    it.copy(
                        latitude = latitude,
                        longitude = longitude
                    )
                }
                latitude to longitude
            }.retry { cause ->
                (cause is MissingLocationPermissionException).also {
                    if (it) delay(MISSING_PERMISSION_RETRY_DURATION)
                }
            }.flatMapLatest<Pair<Double, Double>, WeatherState> { (latitude, longitude) ->
                combine(
                    getWeatherUseCase.execute(
                        lat = latitude.toString(),
                        long = longitude.toString()
                    ),
                    getForecastUseCase.execute(
                        lat = latitude.toString(),
                        long = longitude.toString()
                    )
                ) { weather, forecast ->
                    _uiState.update {
                        it.copy(
                            lastFetchedTime = LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                        )
                    }
                    WeatherState.Success(
                        weather = weather.toWeatherUiModel(),
                        forecast = forecast.toForecastUiModel()
                    )
                }
            }.catch {
                val failure = it as? Failure ?: Failure(throwable = it)
                emit(WeatherState.Error(failure))
            }.onStart { emit(WeatherState.Loading) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WeatherState.Loading
        )

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnLocationPermissionDeclined -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = true
                    )
                }
            }

            is HomeScreenEvent.OnLocationPermissionPermanentlyDeclined -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = true
                    )
                }
            }

            is HomeScreenEvent.OnPermissionDialogDismissed -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = false
                    )
                }
            }

            is HomeScreenEvent.OnTryAgainClick -> {
                viewModelScope.launch {
                    retryTrigger.send(Unit)
                }
            }
        }
    }

    companion object {
        val MISSING_PERMISSION_RETRY_DURATION = 3.seconds
        const val LATITUDE_ARG = "latitude"
        const val LONGITUDE_ARG = "longitude"
    }
}