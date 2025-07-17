package com.sevban.common.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.sevban.common.location.LocationPermissionState
import androidx.core.net.toUri

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasFineLocationPermission(): Boolean = checkSelfPermission(
    Manifest.permission.ACCESS_FINE_LOCATION
) == PackageManager.PERMISSION_GRANTED

fun Context.resolveLocationPermissionState(): LocationPermissionState {
    val hasFine = hasFineLocationPermission()
    val hasCoarse = hasCoarseLocationPermission()
    val hasBackground = hasBackgroundLocationPermission()

    return when {
        hasFine && hasBackground -> LocationPermissionState.BackgroundFine
        hasFine -> LocationPermissionState.ForegroundFine
        hasCoarse -> LocationPermissionState.CoarseOnly
        else -> LocationPermissionState.NoPermission
    }
}

fun Context.hasBackgroundLocationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

fun Context.isLocationEnabled(): Boolean {
    val locationManager =
        getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
    return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
}

fun Context.hasCoarseLocationPermission(): Boolean = checkSelfPermission(
    Manifest.permission.ACCESS_COARSE_LOCATION
) == PackageManager.PERMISSION_GRANTED

fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Context.shouldShowPermissionRationale(permission: String): Boolean {
    return (this as Activity).shouldShowRequestPermissionRationale(permission)
}

fun Context.getVideoUri(videoName: String): Uri {
    val rawId = resources.getIdentifier(videoName, "raw", packageName)
    val videoUri = "android.resource://$packageName/$rawId"
    return videoUri.toUri()
}
