package com.sevban.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sevban.common.extensions.openAppSettings
import com.sevban.home.components.WeatherContent
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.ui.components.ErrorScreen
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester

@Composable
fun HomeScreen(
    weatherState: WeatherState,
    uiState: WeatherScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: (Double, Double) -> Unit,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(
            targetState = weatherState,
            modifier = Modifier.padding(16.dp),
            label = "WeatherAnimatedContent"
        ) {
            when (it) {
                is WeatherState.Error -> ErrorScreen(
                    whenErrorOccurred = whenErrorOccurred,
                    failure = it.failure,
                    onTryAgainClick = { onEvent(HomeScreenEvent.OnTryAgainClick) }
                )

                is WeatherState.Loading -> LoadingScreen(1f)
                is WeatherState.Success -> WeatherContent(
                    weather = it.weather,
                    forecast = it.forecast,
                    onLocationClick = onLocationClick,
                    lastFetchedTime = uiState.lastFetchedTime,
                    onFutureDaysForecastClick = {
                        onFutureDaysForecastClick(
                            uiState.latitude,
                            uiState.longitude
                        )
                    }
                )
            }
        }
    }

    if (uiState.shouldShowPermanentlyDeclinedDialog)
        PermissionAlertDialog(
            onConfirmed = {
                onEvent(HomeScreenEvent.OnPermissionDialogDismissed)
                context.openAppSettings()
            },
            onDismissed = {
                onEvent(HomeScreenEvent.OnPermissionDialogDismissed)
            }
        )

    PermissionRequester(
        onPermissionFirstDeclined = {
            onEvent(HomeScreenEvent.OnLocationPermissionDeclined)
        },
        onPermissionPermanentlyDeclined = {
            onEvent(HomeScreenEvent.OnLocationPermissionPermanentlyDeclined)
        }
    )
}