package com.sevban.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.ui.components.ForecastRow
import com.sevban.ui.model.WeatherUiModel

@Composable
fun WeatherContent(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    lastFetchedTime: String,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentWeatherCard(
            weather = weather,
            onLocationClick = onLocationClick,
            forecast = forecast,
            lastFetchedTime = lastFetchedTime
        )

        HeaderAndMoreBox(
            onFutureDaysForecastClick = onFutureDaysForecastClick,
        )

        ForecastRow(
            forecast = forecast.next24Hours,
        )
    }
}

