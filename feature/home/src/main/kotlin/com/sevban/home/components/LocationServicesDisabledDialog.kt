package com.sevban.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.sevban.designsystem.theme.WeatherAppIcons // Make sure you have a suitable icon, e.g., WeatherAppIcons.LocationOff
import com.sevban.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationServicesDisabledDialog(
    onEnableLocationServicesClick: () -> Unit,
    onChooseAnotherLocationClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(32.dp)
            ) {
                Icon(
                    imageVector = WeatherAppIcons.Location, // Or a "LocationOff" icon if you have one
                    modifier = Modifier.size(96.dp),
                    contentDescription = stringResource(id = R.string.cd_location_services_off_icon),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.location_services_disabled_error))
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onEnableLocationServicesClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.enable_location_services))
                }
                Text(text = stringResource(com.sevban.common.R.string.or))
                OutlinedButton(
                    onClick = onChooseAnotherLocationClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(com.sevban.common.R.string.choose_another_location))
                }
            }
        }
    }
}
