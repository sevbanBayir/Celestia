package com.sevban.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.helper.timeFormatter
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
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

    private val _permissionTrigger = Channel<Unit?>()
    val permissionTrigger = _permissionTrigger.receiveAsFlow()

    private val location = combine(
        savedStateHandle.getStateFlow<Double?>(LATITUDE_ARG, null),
        savedStateHandle.getStateFlow<Double?>(LONGITUDE_ARG, null)
    ) { lat, long ->
        if (lat != null && long != null) {
            lat to long
        } else {
            null
        }
    }.map { savedLocation ->
        val (lat, long) = savedLocation ?: (locationObserver.getCurrentLocation()
            .first().latitude to locationObserver.getCurrentLocation().first().longitude)
        _uiState.update {
            it.copy(
                latitude = lat,
                longitude = long
            )
        }
        lat to long
    }

    val weatherState = retryTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .flatMapLatest {
            location.flatMapLatest<Pair<Double, Double>, WeatherState> { (latitude, longitude) ->
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
                            lastFetchedTime = timeFormatter.format(
                                LocalDateTime.now()
                            )
                        )
                    }
                    WeatherState.Success(
                        weather = weather.toWeatherUiModel(),
                        forecast = forecast.toForecastUiModel()
                    )
                }
            }.catch {
                when (it) {
                    is MissingLocationPermissionException -> {
                        emit(WeatherState.NoLocationPermission)
                    }

                    is Failure -> {
                        emit(WeatherState.Error(it))
                    }

                    else -> {
                        emit(WeatherState.Error(Failure(throwable = it)))
                    }
                }
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
                        isPermissionDeclined = true,
                    )
                }
                _permissionTrigger.trySend(null)
            }

            is HomeScreenEvent.OnLocationPermissionPermanentlyDeclined -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = true,
                        isPermissionPermanentlyDeclined = true
                    )
                }
            }

            is HomeScreenEvent.OnPermanentlyDeclinedDialogDismissed -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = false
                    )
                }
            }

            is HomeScreenEvent.OnTryAgainClick -> {
                retryTrigger.trySend(Unit)
            }

            is HomeScreenEvent.OnGiveLocationPermissionClick -> {
                if (uiState.value.isPermissionPermanentlyDeclined) {
                    _uiState.update {
                        it.copy(
                            shouldShowPermanentlyDeclinedDialog = true
                        )
                    }
                } else {
                    _permissionTrigger.trySend(Unit)
                }
            }

            is HomeScreenEvent.OnLocationPermissionGranted -> {
                retryTrigger.trySend(Unit)
            }
        }
    }

    companion object {
        val MISSING_PERMISSION_RETRY_DURATION = 3.seconds
        const val LATITUDE_ARG = "latitude"
        const val LONGITUDE_ARG = "longitude"
    }
}