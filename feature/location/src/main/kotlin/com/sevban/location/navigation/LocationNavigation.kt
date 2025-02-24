package com.sevban.location.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sevban.location.LocationScreen
import com.sevban.location.LocationScreenViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

fun NavController.navigateToLocationScreen() {
    navigate(Location) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.locationScreen(
    onClickWeather: (lat: Double, long: Double) -> Unit,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
) {
    composable<Location> {
        val viewModel: LocationScreenViewModel = hiltViewModel()
        val locationUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
        val placeListState by viewModel.placeListState.collectAsStateWithLifecycle()
        val error = viewModel.error

        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(key1 = true) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                error.collectLatest {
                    whenErrorOccurred(it, null)
                }
            }
        }

        LocationScreen(
            uiState = locationUiState,
            searchQuery = searchQuery,
            placeListState = placeListState,
            onEvent = viewModel::onEvent,
            onClickWeather = onClickWeather,
        )
    }
}

@Serializable
data object Location
