package com.sevban.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.detail.components.AllDaysForecastContent
import com.sevban.ui.components.ErrorScreen
import com.sevban.ui.components.LoadingScreen

@Composable
fun DetailScreen(
    onEvent: (DetailScreenEvent) -> Unit,
    forecastState: ForecastState,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        when (forecastState) {
            is ForecastState.Success -> AllDaysForecastContent(forecast = forecastState.forecast)
            is ForecastState.Loading -> LoadingScreen(1f)
            is ForecastState.Error -> ErrorScreen(
                whenErrorOccurred = whenErrorOccurred,
                failure = forecastState.failure,
                onTryAgainClick = { onEvent(DetailScreenEvent.OnTryAgainClick) }
            )
        }
    }
}

