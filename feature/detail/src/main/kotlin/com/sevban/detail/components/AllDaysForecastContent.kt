package com.sevban.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.detail.R
import com.sevban.detail.mapper.ForecastUiModel
import com.sevban.ui.components.ForecastRow

@Composable
fun AllDaysForecastContent(
    forecast: ForecastUiModel,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(R.string.today),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(16.dp))
        ForecastRow(forecast.today)
        Spacer(Modifier.height(16.dp))
        OtherDaysForecastContent(forecast.eachDayWithAverage)
    }
}