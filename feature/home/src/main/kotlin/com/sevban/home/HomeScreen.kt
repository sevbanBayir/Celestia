package com.sevban.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sevban.common.extensions.openAppSettings
import com.sevban.home.components.NoLocationPermissionDialog
import com.sevban.home.components.WeatherContent
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.ui.components.ErrorScreen
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester
import com.sevban.ui.model.LocationArgument

// TODO: FIX VIDEO ASSET DELIVERY
// TODO: FIX CACHING
@Composable
fun HomeScreen(
    weatherState: WeatherState,
    uiState: WeatherScreenUiState,
    permissionTrigger: Unit?,
    onEvent: (HomeScreenEvent) -> Unit,
    onLocationClick: (LocationArgument) -> Unit,
    onFutureDaysForecastClick: (LocationArgument) -> Unit,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(
            targetState = weatherState,
            label = "WeatherAnimatedContent"
        ) {
            when (it) {
                is WeatherState.NoLocationPermission -> NoLocationPermissionDialog(
                    onGiveLocationPermissionClick = { onEvent(HomeScreenEvent.OnGiveLocationPermissionClick) },
                    onChooseAnotherLocationClick = { onLocationClick(LocationArgument(uiState.latitude, uiState.longitude)) },
                    onDismiss = { }
                )

                is WeatherState.Error -> ErrorScreen(
                    whenErrorOccurred = whenErrorOccurred,
                    failure = it.failure,
                    onTryAgainClick = { onEvent(HomeScreenEvent.OnTryAgainClick) }
                )

                is WeatherState.Loading -> LoadingScreen(1f)
                is WeatherState.Success -> WeatherContent(
                    weather = it.weather,
                    forecast = it.forecast,
                    onLocationClick = { onLocationClick(LocationArgument(uiState.latitude, uiState.longitude)) },
                    lastFetchedTime = uiState.lastFetchedTime,
                    onFutureDaysForecastClick = {
                        onFutureDaysForecastClick(
                            LocationArgument(
                                uiState.latitude,
                                uiState.longitude
                            )
                        )
                    },
                )
            }
        }
    }

    if (uiState.shouldShowPermanentlyDeclinedDialog)
        PermissionAlertDialog(
            onConfirmed = {
                onEvent(HomeScreenEvent.OnPermanentlyDeclinedDialogDismissed)
                context.openAppSettings()
            },
            onDismissed = {
                onEvent(HomeScreenEvent.OnPermanentlyDeclinedDialogDismissed)
            }
        )

    if (permissionTrigger != null)
        PermissionRequester(
            onPermissionGranted = {
                onEvent(HomeScreenEvent.OnLocationPermissionGranted)
            },
            onPermissionFirstDeclined = {
                onEvent(HomeScreenEvent.OnLocationPermissionDeclined)
            },
            onPermissionPermanentlyDeclined = {
                onEvent(HomeScreenEvent.OnLocationPermissionPermanentlyDeclined)
            }
        )
}