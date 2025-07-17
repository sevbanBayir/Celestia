package com.sevban.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sevban.ui.R

@Composable
fun PermissionAlertDialog(
    onConfirmed: () -> Unit,
    onDismissed: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissed,
        confirmButton = {
            Button(onClick = onConfirmed) {
                Text(text = stringResource(R.string.permission_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissed) {
                Text(text = stringResource(R.string.permission_dialog_dismiss))
            }
        },
        title = {
            Text(text = stringResource(R.string.permission_dialog_title))
        },
        text = {
            Text(text = stringResource(R.string.permission_dialog_message))
        }
    )
}