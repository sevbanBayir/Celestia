package com.sevban.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.ui.model.WeatherUiModel

@Composable
fun CurrentWeatherCard(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    lastFetchedTime: String,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxSize()) {
            /* TODO: Will be activated when video assets are added
            if (weather.video != null) {
                VideoPlayer(
                    videoName = weather.video!!,
                    modifier = Modifier
                        .heightIn(min = 570.dp)
                        .fillMaxHeight()
                )
            }
             */
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                LocationAndFetchTimeBox(
                    modifier = Modifier.padding(12.dp),
                    cityName = weather.cityName,
                    lastFetchedTime = lastFetchedTime,
                    onLocationClick = onLocationClick
                )

                TemperatureAndDescription(
                    temp = weather.temp,
                    description = weather.description
                )

                WeatherFeaturesCard(
                    windSpeed = weather.windSpeed,
                    humidity = weather.humidity,
                    visibility = weather.visibility,
                    modifier = Modifier.padding(12.dp)
                )

                LineChartContainer(
                    xAxisData = forecast.chartData.dateList,
                    yAxisData = forecast.chartData.temperatures,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}