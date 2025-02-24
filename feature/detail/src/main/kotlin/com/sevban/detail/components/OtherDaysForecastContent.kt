package com.sevban.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.detail.R
import com.sevban.detail.mapper.EachDayWithAverage

@Composable
fun OtherDaysForecastContent(
    forecast: List<EachDayWithAverage>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        forecast.forEachIndexed { index, it ->
            DayForecast(
                dayOfWeek = if (index == 0) stringResource(R.string.tomorrow) else it.dayOfWeek,
                dayOfMonth = it.dayOfMonth,
                iconUrl = it.icon,
                description = it.description,
                temperature = it.temperature
            )
        }
    }
}