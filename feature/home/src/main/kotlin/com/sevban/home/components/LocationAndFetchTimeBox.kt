package com.sevban.home.components

import androidx.compose.foundation.layout.Box
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
    modifier: Modifier = Modifier
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
                text = cityName,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

/*        Text(
            text = stringResource(id = R.string.last_fetched_at, lastFetchedTime),
            modifier = Modifier.align(Alignment.CenterEnd),
            style = MaterialTheme.typography.labelSmall
        )*/
    }
}