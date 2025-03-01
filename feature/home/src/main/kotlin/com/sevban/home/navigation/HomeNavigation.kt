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
import com.sevban.ui.model.LocationArgument
import com.sevban.ui.model.locationArgumentNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

fun NavController.navigateToHome(
    location: LocationArgument?,
    navOptions: NavOptions? = null
) {
    navigate(Home(location), navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    onLocationClick: (LocationArgument) -> Unit,
    onFutureDaysForecastClick: (LocationArgument) -> Unit
) {
    composable<Home>(typeMap = Home.typeMap) {
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
data class Home(val location: LocationArgument? = null) {
    companion object {
        val typeMap = mapOf(typeOf<LocationArgument?>() to locationArgumentNavType)
    }
}

