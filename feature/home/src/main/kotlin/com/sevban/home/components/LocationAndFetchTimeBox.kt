package com.sevban.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.designsystem.theme.WeatherAppIcons
import com.sevban.home.R

@Composable
fun LocationAndFetchTimeBox(
    cityName: String,
    lastFetchedTime: String,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
    preferredLocationName: String? = null, // User's selected location name
    dataAge: String? = null // Data age (e.g., "5 min ago")
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = onLocationClick,
            modifier = Modifier.align(Alignment.CenterStart),
            contentPadding = PaddingValues(0.dp),
        ) {
            Icon(
                imageVector = WeatherAppIcons.Location,
                modifier = Modifier.size(24.dp),
                contentDescription = stringResource(id = R.string.cd_location_icon)
            )
            Text(
                text = preferredLocationName ?: cityName,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        // Show cached data age in the opposite corner
        if (dataAge != null) {
            Text(
                text = dataAge,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}