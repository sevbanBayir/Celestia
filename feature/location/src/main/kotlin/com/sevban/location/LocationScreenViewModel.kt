package com.sevban.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.extensions.EMPTY
import com.sevban.common.location.Geocoder
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.location.helper.PlaceAutocompleteService
import com.sevban.location.model.LocationScreenUiState
import com.sevban.location.model.PlaceListState
import com.sevban.ui.model.toWeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LocationScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val placeAutocompleteService: PlaceAutocompleteService,
    private val geocoder: Geocoder
) : ViewModel() {

    private val _error = Channel<Throwable>()
    val error = _error.receiveAsFlow()

    private val _uiState = MutableStateFlow(LocationScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow(String.EMPTY)
    val searchQuery = _searchQuery.asStateFlow()

    val placeListState: StateFlow<PlaceListState> = _searchQuery
        .onEach { _uiState.update { it.copy(isPlaceListLoading = true) } }
        .flatMapLatest {
            if (it.isBlank()) flowOf(null)
            else placeAutocompleteService.getAutocomplete(it)
        }
        .catch { _error.send(it) }
        .mapLatest { placeTexts ->
            if (placeTexts == null) {
                _uiState.update { it.copy(isPlaceListLoading = false) }
                return@mapLatest PlaceListState.Initial
            }

            placeTexts.mapNotNull { searchQuery ->
                geocoder.getCityCoordinates(searchQuery)
            }.let { places ->
                if (places.isEmpty()) PlaceListState.Empty
                else PlaceListState.Success(places)
            }.also {
                _uiState.update { it.copy(isPlaceListLoading = false) }
            }
        }
        .catch { _error.send(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlaceListState.Initial
        )

    fun onEvent(event: LocationScreenEvent) {
        when (event) {
            is LocationScreenEvent.OnSearchQueryChanged -> {
                _searchQuery.update { event.query }
            }

            is LocationScreenEvent.OnLocationSelected -> {
                _uiState.update { it.copy(selectedPlace = event.prediction) }
                _searchQuery.update { String.EMPTY }
                viewModelScope.launch {
                    getWeatherUseCase.execute(
                        event.prediction.latitude.toString(),
                        event.prediction.longitude.toString()
                    )
                        .catch { _error.send(it) }
                        .collect { weather ->
                            _uiState.update { it.copy(weather = weather.toWeatherUiModel()) }
                        }

                }
            }
        }
    }
}