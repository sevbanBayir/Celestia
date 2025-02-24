package com.sevban.location.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.sevban.model.Place
import com.sevban.ui.model.WeatherUiModel

@Composable
fun PlaceWeatherMarker(
    modifier: Modifier = Modifier,
    markerLocation: Place,
    weatherForSelectedLocation: WeatherUiModel,
    onMarkerClick: () -> Unit,
) {
    val markerState = remember(markerLocation) {
        MarkerState(
            position = LatLng(
                markerLocation.latitude,
                markerLocation.longitude
            )
        )
    }

    val asyncImagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(weatherForSelectedLocation.iconUrl)
            .size(100.dp.value.toInt())
            .allowHardware(false)
            .build(),
    )

    MarkerComposable(
        arrayOf(
            asyncImagePainter.state,
            weatherForSelectedLocation,
            markerState
        ),
        state = markerState,
        onClick = {
            onMarkerClick()
            false
        }
    ) {
        Column(
            modifier = modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Card(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = markerLocation.cityName,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = asyncImagePainter,
                        modifier = Modifier.size(50.dp),
                        contentDescription = null
                    )
                    Text(
                        text = "${weatherForSelectedLocation.temp} ֯",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}