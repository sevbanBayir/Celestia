package com.sevban.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sevban.common.location.LocationObserver
import com.sevban.common.location.LocationServicesDisabledException
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.common.model.Failure
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.home.mapper.ForecastUiModelMapper
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.home.navigation.Home
import com.sevban.model.LocationType
import com.sevban.ui.mapper.WeatherUiModelMapper
import com.sevban.ui.model.LocationArgument
import com.sevban.ui.model.toLocationArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val locationObserver: LocationObserver,
    private val weatherMapper: WeatherUiModelMapper,
    private val forecastMapper: ForecastUiModelMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val retryTrigger = Channel<Unit>()

    private val _permissionTrigger = Channel<Unit?>()
    val permissionTrigger = _permissionTrigger.receiveAsFlow()

    private val savedLocation = savedStateHandle.toRoute<Home>(typeMap = Home.typeMap).location

    val weatherState = retryTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .flatMapLatest {
            getLocation()
                .filterNotNull()
                .flatMapLatest { location ->
                    combine(
                        getWeatherUseCase.execute(
                            lat = location.latitude.toString(),
                            long = location.longitude.toString()
                        ),
                        getForecastUseCase.execute(
                            lat = location.latitude.toString(),
                            long = location.longitude.toString()
                        ),
                        transform = { weather, forecast ->
                            WeatherState.Success(
                                weather = weatherMapper.mapToUiModel(weather),
                                forecast = forecastMapper.mapToUiModel(forecast)
                            )
                        }
                    )
                }
                .catch<WeatherState> { exception ->
                    when (exception) {
                        is MissingLocationPermissionException -> {
                            emit(WeatherState.NoLocationPermission)
                        }
                        is LocationServicesDisabledException -> {
                            emit(WeatherState.LocationServicesDisabled)
                        }

                        is Failure -> {
                            emit(WeatherState.Error(exception))
                        }

                        else -> {
                            emit(WeatherState.Error(Failure(throwable = exception)))
                        }
                    }
                }
                .onStart { emit(WeatherState.Loading) }
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

            is HomeScreenEvent.OnTryAgainClick,
            is HomeScreenEvent.OnLocationServicesEnabled,
            is HomeScreenEvent.OnLocationPermissionGranted -> {
                retryTrigger.trySend(Unit)
            }
        }
    }

    private fun getLocation(): Flow<LocationArgument?> = flow {
        if (savedLocation != null) {
            // Use user's manually selected location
            emit(savedLocation)
            _uiState.update {
                it.copy(
                    location = savedLocation,
                    preferredLocationName = savedLocation.locationName
                )
            }
            return@flow
        }

        val lastKnownLocation = locationObserver.getLastKnownLocation().first()

        if (lastKnownLocation != null) {
            val locationAge = System.currentTimeMillis() - (lastKnownLocation.locationType as LocationType.LastKnown).timestamp
            val isLocationStale = locationAge > LOCATION_STALENESS_THRESHOLD_MS

            val locationArgument = lastKnownLocation.toLocationArgument()

            emit(locationArgument)
            _uiState.update {
                it.copy(
                    location = locationArgument,
                    preferredLocationName = locationArgument.locationName
                )
            }

            if (isLocationStale) {
                val freshLocation =
                    locationObserver.getCurrentLocation().first()?.toLocationArgument()
                emit(freshLocation)
                _uiState.update {
                    it.copy(
                        location = freshLocation,
                        preferredLocationName = freshLocation?.locationName
                    )
                }
            }
            return@flow
        }


        val currentLocation = locationObserver.getCurrentLocation().first()?.toLocationArgument()
        emit(currentLocation)
        _uiState.update {
            it.copy(
                location = currentLocation,
                preferredLocationName = currentLocation?.locationName
            )
        }
    }

    companion object {
        private const val LOCATION_STALENESS_THRESHOLD_MS = 30 * 60 * 1000L
    }
}