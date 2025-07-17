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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.common.helper.WeatherLocalizationService
import com.sevban.designsystem.theme.WeatherAppIcons
import com.sevban.home.R
import kotlinx.coroutines.delay

@Composable
fun LocationAndFetchTimeBox(
    cityName: String,
    lastUpdated: Long,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
    preferredLocationName: String? = null // User's selected location name
) {
    val context = LocalContext.current
    val localizationService = remember(context) { WeatherLocalizationService(context) }

    var dataAge by remember { mutableStateOf(localizationService.formatDataAge(lastUpdated)) }

    LaunchedEffect(lastUpdated) {
        while (true) {
            dataAge = localizationService.formatDataAge(lastUpdated)
            delay(60_000)
        }
    }

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

        if (dataAge.isNotEmpty()) {
            Text(
                text = dataAge,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}