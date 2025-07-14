package com.sevban.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import com.sevban.home.mapper.ChartData
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.ui.model.WeatherUiModel

@Composable
fun AdditionalWeatherInfoSection(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeelsLikeCard(
                actualTemp = weather.temp,
                feelsLike = weather.feelsLike,
                modifier = Modifier.weight(1f)
            )

            PressureCard(
                pressure = weather.pressureWithUnit,
                pressureStatus = weather.pressureStatus,
                modifier = Modifier.weight(1f)
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RainCard(
                rainInfo = weather.rainInfo,
                isRaining = weather.isRaining,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            
            PrecipitationCard(
                precipitationChance = forecast.precipitationChance,
                nextRainTime = forecast.nextRainTime,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
private fun AdditionalWeatherInfoSectionPreview() {
    ComposeScaffoldProjectTheme {
        AdditionalWeatherInfoSection(
            weather = WeatherUiModel(
                id = 800,
                description = "Clear Sky",
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                cityName = "New York",
                feelsLike = "25",
                grndLevel = "1012",
                humidity = "65",
                pressure = "1013",
                seaLevel = "1015",
                temp = "22",
                tempMax = "26",
                tempMin = "18",
                visibility = "10000",
                windSpeed = "15",
                video = "clear_sky",
                pressureStatus = "Normal",
                pressureWithUnit = "1013 hPa",
                tempRange = "26° / 18°",
                rainInfo = "0.5 mm/h",
                isRaining = true
            ),
            forecast = ForecastUiModel(
                city = "New York",
                forecastBy3Hours = emptyList(),
                chartData = ChartData(emptyList(), emptyList()),
                next24Hours = emptyList(),
                todayHigh = "26",
                todayLow = "18",
                precipitationChance = "70%",
                nextRainTime = "In 2 hours"
            )
        )
    }
} 