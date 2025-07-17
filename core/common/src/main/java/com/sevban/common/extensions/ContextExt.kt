package com.sevban.common.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

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

fun Context.hasBackgroundLocationPermission(): Boolean = checkSelfPermission(
    Manifest.permission.ACCESS_BACKGROUND_LOCATION
) == PackageManager.PERMISSION_GRANTED

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
    return Uri.parse(videoUri)
}
