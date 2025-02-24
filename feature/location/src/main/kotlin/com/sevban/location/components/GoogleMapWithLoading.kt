package com.sevban.location.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.sevban.model.Place
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.model.WeatherUiModel

@Composable
fun GoogleMapWithLoading(
    mapColorScheme: ComposeMapColorScheme,
    properties: MapProperties,
    onMapLoaded: () -> Unit,
    mapCameraPositionState: CameraPositionState,
    markerLocation: Place?,
    weatherForSelectedLocation: WeatherUiModel?,
    isLoading: Boolean,
    onMarkerClick: (lat: Double, long: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    GoogleMap(
        modifier = modifier,
        mapColorScheme = mapColorScheme,
        properties = properties,
        onMapLoaded = onMapLoaded,
        cameraPositionState = mapCameraPositionState,
    ) {
        if (markerLocation != null && weatherForSelectedLocation != null)
            PlaceWeatherMarker(
                markerLocation = markerLocation,
                weatherForSelectedLocation = weatherForSelectedLocation,
                onMarkerClick = {
                    onMarkerClick(
                        markerLocation.latitude,
                        markerLocation.longitude
                    )
                },
            )
    }
    AnimatedVisibility(
        visible = isLoading,
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        LoadingScreen(
            heightFraction = 1f,
            backgroundColor = MaterialTheme.colorScheme.background
        )
    }
}