package com.sevban.location

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.sevban.location.components.GoogleMapWithLoading
import com.sevban.location.components.SearchbarWithList
import com.sevban.location.model.LocationScreenUiState
import com.sevban.location.model.PlaceListState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(
    uiState: LocationScreenUiState,
    searchQuery: String,
    placeListState: PlaceListState,
    onEvent: (LocationScreenEvent) -> Unit,
    onClickWeather: (lat: Double, long: Double) -> Unit,
) {
    val context = LocalContext.current
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val mapColorScheme = remember(isSystemInDarkTheme) {
        if (isSystemInDarkTheme) ComposeMapColorScheme.DARK
        else ComposeMapColorScheme.LIGHT
    }

    val properties = remember {
        MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
        )
    }

    val mapCameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    var isMapLoading by remember {
        mutableStateOf(true)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.selectedPlace) {
        if (uiState.selectedPlace != null) {
            val cameraPosition = CameraPosition.builder()
                .target(LatLng(uiState.selectedPlace.latitude, uiState.selectedPlace.longitude))
                .zoom(7f)
                .build()

            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            coroutineScope.launch { mapCameraPositionState.animate(cameraUpdate, 1500) }
        }
    }

    LaunchedEffect(mapCameraPositionState.isMoving) {
        if (!mapCameraPositionState.isMoving)
            keyboardController?.hide()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        GoogleMapWithLoading(
            mapColorScheme = mapColorScheme,
            modifier = Modifier,
            properties = properties,
            isLoading = isMapLoading,
            mapCameraPositionState = mapCameraPositionState,
            onMapLoaded = { isMapLoading = false },
            markerLocation = uiState.selectedPlace,
            onMarkerClick = onClickWeather,
            weatherForSelectedLocation = uiState.weather,
        )

        if (!isMapLoading)
            SearchbarWithList(
                searchQuery = searchQuery,
                isPlaceListLoading = uiState.isPlaceListLoading,
                placeListState = placeListState,
                onSearchQueryChanged = { onEvent(LocationScreenEvent.OnSearchQueryChanged(it)) },
                onPlaceClick = { onEvent(LocationScreenEvent.OnLocationSelected(it)) },
                modifier = Modifier.align(Alignment.TopCenter)
            )
    }
}

