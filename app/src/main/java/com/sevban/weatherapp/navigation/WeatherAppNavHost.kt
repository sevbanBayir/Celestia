package com.sevban.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.sevban.detail.detailScreen
import com.sevban.detail.navigateToDetail
import com.sevban.home.navigation.Home
import com.sevban.home.navigation.homeScreen
import com.sevban.home.navigation.navigateToHome
import com.sevban.location.navigation.locationScreen
import com.sevban.location.navigation.navigateToLocationScreen
import com.sevban.weatherapp.AppState

@Composable
fun WeatherAppNavHost(
    appState: AppState,
    onShowSnackbar: suspend (Throwable, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = Home(),
        modifier = modifier,
    ) {
        homeScreen(
            whenErrorOccurred = onShowSnackbar,
            onLocationClick = navController::navigateToLocationScreen,
            onFutureDaysForecastClick = navController::navigateToDetail
        )

        detailScreen(
            whenErrorOccured = onShowSnackbar,
        )

        locationScreen(
            whenErrorOccurred = onShowSnackbar,
            onClickWeather = { lat, long ->
                val navOptions = navOptions {
                    launchSingleTop = true
                }
                navController.navigateToHome(lat, long, navOptions)
            }
        )
    }
}
