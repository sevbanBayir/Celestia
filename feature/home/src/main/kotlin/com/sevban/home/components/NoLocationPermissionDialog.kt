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
import com.sevban.designsystem.theme.WeatherAppIcons
import com.sevban.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoLocationPermissionDialog(
    onGiveLocationPermissionClick: () -> Unit,
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
                    imageVector = WeatherAppIcons.Location,
                    modifier = Modifier.size(96.dp),
                    contentDescription = stringResource(id = R.string.cd_location_icon),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(com.sevban.common.R.string.location_error))
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onGiveLocationPermissionClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(com.sevban.common.R.string.give_location_permission))
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