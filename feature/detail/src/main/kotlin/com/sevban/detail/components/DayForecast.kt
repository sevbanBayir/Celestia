package com.sevban.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sevban.common.extensions.toTitleCase
import com.sevban.ui.R

@Composable
fun DayForecast(
    modifier: Modifier = Modifier,
    dayOfWeek: String,
    dayOfMonth: String,
    iconUrl: String,
    description: String,
    temperature: Int
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                dayOfWeek.lowercase().toTitleCase(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(dayOfMonth)
        }
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.temperature_celsius, temperature),
            style = MaterialTheme.typography.titleLarge
        )
        AsyncImage(
            model = iconUrl,
            contentDescription = description
        )
    }
}