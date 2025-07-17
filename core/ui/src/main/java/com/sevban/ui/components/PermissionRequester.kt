package com.sevban.ui.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.sevban.common.extensions.hasLocationPermission
import com.sevban.common.extensions.shouldShowPermissionRationale

@Composable
fun PermissionRequester(
    onPermissionGranted: () -> Unit = {},
    onPermissionFirstDeclined: () -> Unit = {},
    onPermissionPermanentlyDeclined: () -> Unit = {},
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    var askForBgLocationPermission by remember { mutableStateOf(false) }
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissionResults ->

                permissions.forEach { permission ->
                    if (permissionResults[permission] == true) {
                        onPermissionGranted()
                        askForBgLocationPermission = true
                    } else if (context.shouldShowPermissionRationale(permission)
                            .not() && context.hasLocationPermission().not()
                    ) {
                        onPermissionPermanentlyDeclined()
                    } else {
                        onPermissionFirstDeclined()
                    }
                }
            }
        )

    val singlePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permissionResult ->
            if (permissionResult) {

            }
        }
    )



    LaunchedEffect(key1 = askForBgLocationPermission) {
        if (askForBgLocationPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                singlePermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            askForBgLocationPermission = false
        }
    }

    LaunchedEffect(key1 = true) {
        activityResultLauncher.launch(permissions)
    }
}