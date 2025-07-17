package com.sevban.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import com.sevban.home.mapper.ChartData
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.ui.components.ForecastRow
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.WeatherUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: () -> Unit,
    modifier: Modifier = Modifier,
    preferredLocationName: String? = null
) {
    val scrollState = rememberScrollState()

    PullToRefreshBox(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
    ) {
        Column(
            modifier = modifier.verticalScroll(state = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CurrentWeatherCard(
                weather = weather,
                onLocationClick = onLocationClick,
                forecast = forecast,
                preferredLocationName = preferredLocationName
            )

            HeaderAndMoreBox(
                onFutureDaysForecastClick = onFutureDaysForecastClick,
            )

            ForecastRow(
                forecast = forecast.next24Hours,
            )

            // NEW: Additional weather features section
            AdditionalWeatherInfoSection(
                weather = weather,
                forecast = forecast,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun WeatherContentPreview() {
    ComposeScaffoldProjectTheme {
        WeatherContent(
            weather = createMockWeatherUiModel(),
            forecast = createMockForecastUiModel(),
            onLocationClick = { },
            onFutureDaysForecastClick = { },
            onRefresh = { },
            isRefreshing = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherContentWithLocationNamePreview() {
    ComposeScaffoldProjectTheme {
        WeatherContent(
            weather = createMockWeatherUiModel(),
            forecast = createMockForecastUiModel(),
            onLocationClick = { },
            onFutureDaysForecastClick = { },
            preferredLocationName = "Central Park",
            onRefresh = { },
            isRefreshing = false
        )
    }
}

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
    lastUpdated = System.currentTimeMillis()
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
    lastUpdated = System.currentTimeMillis()
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

