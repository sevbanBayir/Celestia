package com.sevban.home.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sevban.home.HomeScreen
import com.sevban.home.HomeViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToHome(
    latitude: Double? = null,
    longitude: Double? = null,
    navOptions: NavOptions? = null
) {
    navigate(Home(latitude, longitude), navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: (Double, Double) -> Unit
) {
    composable<Home> {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
        val permissionTrigger by viewModel.permissionTrigger.collectAsStateWithLifecycle(null)

        HomeScreen(
            uiState = homeUiState,
            permissionTrigger = permissionTrigger,
            whenErrorOccurred = whenErrorOccurred,
            onEvent = viewModel::onEvent,
            onLocationClick = onLocationClick,
            weatherState = weatherState,
            onFutureDaysForecastClick = onFutureDaysForecastClick
        )
    }
}

@Serializable
data class Home(val latitude: Double? = null, val longitude: Double? = null)

