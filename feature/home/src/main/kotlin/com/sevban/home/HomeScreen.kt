package com.sevban.home

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.location.AndroidLocationObserver.Companion.promptEnableLocationIfNeeded
import com.sevban.common.model.ErrorType
import com.sevban.common.model.Failure
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import com.sevban.home.components.LocationServicesDisabledDialog
import com.sevban.home.components.NoLocationPermissionDialog
import com.sevban.home.components.WeatherContent
import com.sevban.home.mapper.ChartData
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.ui.components.ErrorScreen
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.LocationArgument
import com.sevban.ui.model.WeatherUiModel

// TODO: FIX VIDEO ASSET DELIVERY
// TODO: FIX CACHING
@Composable
fun HomeScreen(
    weatherState: WeatherState,
    uiState: WeatherScreenUiState,
    isRefreshing: Boolean,
    permissionTrigger: Unit?,
    onEvent: (HomeScreenEvent) -> Unit,
    onLocationClick: (LocationArgument?) -> Unit,
    onFutureDaysForecastClick: (LocationArgument) -> Unit,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onEvent(HomeScreenEvent.OnLocationServicesEnabled)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {

        when (weatherState) {
            is WeatherState.NoLocationPermission -> NoLocationPermissionDialog(
                onGiveLocationPermissionClick = { onEvent(HomeScreenEvent.OnGiveLocationPermissionClick) },
                onChooseAnotherLocationClick = { onLocationClick(uiState.location) },
                onDismiss = { }
            )

            is WeatherState.LocationServicesDisabled -> LocationServicesDisabledDialog(
                onEnableLocationServicesClick = {
                    activity?.promptEnableLocationIfNeeded(
                        onResolutionRequired = { intentSender ->
                            launcher.launch(IntentSenderRequest.Builder(intentSender).build())
                        },
                        onAlreadyEnabled = { },
                        onNotResolvable = { _ -> }
                    )
                },
                onChooseAnotherLocationClick = { onLocationClick(uiState.location) },
                onDismiss = { }
            )

            is WeatherState.Error -> ErrorScreen(
                whenErrorOccurred = whenErrorOccurred,
                failure = weatherState.failure,
                onTryAgainClick = { onEvent(HomeScreenEvent.OnTryAgainClick) }
            )

            is WeatherState.Loading -> LoadingScreen(1f)
            is WeatherState.Success -> WeatherContent(
                weather = weatherState.weather,
                forecast = weatherState.forecast,
                onLocationClick = { onLocationClick(uiState.location) },
                onFutureDaysForecastClick = { onFutureDaysForecastClick(uiState.location!!) },
                preferredLocationName = uiState.preferredLocationName,
                isRefreshing = isRefreshing,
                onRefresh = { onEvent(HomeScreenEvent.OnTryAgainClick) }
            )
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

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun HomeScreenSuccessPreview() {
    ComposeScaffoldProjectTheme {
        HomeScreen(
            weatherState = WeatherState.Success(
                weather = createMockWeatherUiModel(),
                forecast = createMockForecastUiModel()
            ),
            uiState = createMockUiState(),
            permissionTrigger = null,
            onEvent = { },
            onLocationClick = { },
            onFutureDaysForecastClick = { },
            whenErrorOccurred = { _, _ -> },
            isRefreshing = false
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun HomeScreenLoadingPreview() {
    ComposeScaffoldProjectTheme {
        HomeScreen(
            weatherState = WeatherState.Loading,
            uiState = createMockUiState(),
            permissionTrigger = null,
            onEvent = { },
            onLocationClick = { },
            onFutureDaysForecastClick = { },
            whenErrorOccurred = { _, _ -> },
            isRefreshing = false
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun HomeScreenErrorPreview() {
    ComposeScaffoldProjectTheme {
        HomeScreen(
            weatherState = WeatherState.Error(
                Failure(
                    errorType = ErrorType.CONNECTIVITY_ERROR
                )
            ),
            uiState = createMockUiState(),
            permissionTrigger = null,
            onEvent = { },
            onLocationClick = { },
            onFutureDaysForecastClick = { },
            whenErrorOccurred = { _, _ -> },
            isRefreshing = false
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun HomeScreenNoLocationPermissionPreview() {
    ComposeScaffoldProjectTheme {
        HomeScreen(
            weatherState = WeatherState.NoLocationPermission,
            uiState = createMockUiState(),
            permissionTrigger = null,
            onEvent = { },
            onLocationClick = { },
            onFutureDaysForecastClick = { },
            whenErrorOccurred = { _, _ -> },
            isRefreshing = false
        )
    }
}

private fun createMockUiState() = WeatherScreenUiState(
    shouldShowPermanentlyDeclinedDialog = false,
    isPermissionDeclined = false,
    isPermissionPermanentlyDeclined = false,
    lastFetchedTime = "Last updated: 12:30 PM",
    location = LocationArgument(40.7128, -74.0060, "New York"),
    preferredLocationName = "Central Park"
)

private fun createMockWeatherUiModel() = WeatherUiModel(
    id = 800,
    description = "Clear Sky",
    iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
    cityName = "New York",
    feelsLike = "28",
    grndLevel = "1012",
    humidity = "65",
    pressure = "1015",
    seaLevel = "1015",
    temp = "25",
    tempMax = "28",
    tempMin = "22",
    visibility = "10000",
    windSpeed = "5.2",
    video = "clear_sky",
    pressureStatus = "Normal",
    pressureWithUnit = "1015 hPa",
    tempRange = "28° / 22°",
    rainInfo = "0.0 mm/h",
    isRaining = false,
    lastUpdated = System.currentTimeMillis(),
)

private fun createMockForecastUiModel() = ForecastUiModel(
    city = "New York",
    forecastBy3Hours = createMockForecastList(),
    chartData = ChartData(
        temperatures = listOf(25, 27, 29, 26, 24, 22, 21, 23),
        dateList = listOf("12:00", "15:00", "18:00", "21:00", "00:00", "03:00", "06:00", "09:00")
    ),
    next24Hours = createMockNext24Hours(),
    todayHigh = "28",
    todayLow = "22",
    precipitationChance = "15%",
    nextRainTime = "",
    lastUpdated = System.currentTimeMillis(),
)

private fun createMockForecastList() = listOf(
    ForecastWeatherUi(25, "12:00", "https://openweathermap.org/img/wn/01d@2x.png", "Clear"),
    ForecastWeatherUi(27, "15:00", "https://openweathermap.org/img/wn/01d@2x.png", "Clear"),
    ForecastWeatherUi(29, "18:00", "https://openweathermap.org/img/wn/02d@2x.png", "Partly Cloudy"),
    ForecastWeatherUi(26, "21:00", "https://openweathermap.org/img/wn/02n@2x.png", "Partly Cloudy")
)

private fun createMockNext24Hours() = listOf(
    ForecastWeatherUi(25, "Now", "https://openweathermap.org/img/wn/01d@2x.png", "Clear"),
    ForecastWeatherUi(26, "13:00", "https://openweathermap.org/img/wn/01d@2x.png", "Clear"),
    ForecastWeatherUi(28, "14:00", "https://openweathermap.org/img/wn/01d@2x.png", "Clear"),
    ForecastWeatherUi(27, "15:00", "https://openweathermap.org/img/wn/02d@2x.png", "Partly Cloudy"),
    ForecastWeatherUi(24, "16:00", "https://openweathermap.org/img/wn/02d@2x.png", "Partly Cloudy")
)