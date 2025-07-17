package com.sevban.common.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.sevban.common.extensions.isLocationEnabled
import com.sevban.common.extensions.resolveLocationPermissionState
import com.sevban.common.location.mapper.toDomainLocation
import com.sevban.model.DomainLocation
import com.sevban.model.LocationType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")
class AndroidLocationObserver @Inject constructor(
    private val context: Context,
) : LocationObserver {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    override fun getCurrentLocation(): Flow<DomainLocation?> = callbackFlow {
        when (context.resolveLocationPermissionState()) {
            is LocationPermissionState.NoPermission -> {
                close(MissingLocationPermissionException())
                return@callbackFlow
            }

            else -> {
                if (!context.isLocationEnabled()) {
                    close(LocationServicesDisabledException())
                    return@callbackFlow
                }
            }
        }

        val request = when (context.resolveLocationPermissionState()) {
            is LocationPermissionState.CoarseOnly -> CurrentLocationRequest.Builder()
                .setGranularity(Granularity.GRANULARITY_COARSE)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .build()

            else -> CurrentLocationRequest.Builder()
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()
        }

        client.getCurrentLocation(request, null)
            .addOnSuccessListener { location ->
                trySend(location?.toDomainLocation(locationType = LocationType.Current))
                close()
            }
            .addOnFailureListener { close(it) }

        awaitClose()
    }

    override fun getLastKnownLocation(): Flow<DomainLocation?> = callbackFlow {
        when (context.resolveLocationPermissionState()) {
            is LocationPermissionState.NoPermission -> {
                close(MissingLocationPermissionException())
                return@callbackFlow
            }

            else -> {
                if (!context.isLocationEnabled()) {
                    close(LocationServicesDisabledException())
                    return@callbackFlow
                }
            }
        }

        client.lastLocation
            .addOnSuccessListener { location ->
                trySend(location?.toDomainLocation(locationType = LocationType.LastKnown(timestamp = location.time)))
                close()
            }
            .addOnFailureListener {
                close(it)
            }

        awaitClose()
    }

    companion object {
        fun Activity.promptEnableLocationIfNeeded(
            onResolutionRequired: (IntentSender) -> Unit,
            onAlreadyEnabled: () -> Unit,
            onNotResolvable: (Exception) -> Unit
        ) {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000L
            ).build()

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)

            val settingsClient = LocationServices.getSettingsClient(this)
            val task = settingsClient.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                onAlreadyEnabled()
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        onResolutionRequired(exception.resolution.intentSender)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        onNotResolvable(sendEx)
                    }
                } else {
                    onNotResolvable(exception)
                }
            }
        }
    }
}

sealed class LocationPermissionState {
    object NoPermission : LocationPermissionState()
    object CoarseOnly : LocationPermissionState()
    object ForegroundFine : LocationPermissionState()
    object BackgroundFine : LocationPermissionState()
}

class LocationServicesDisabledException : Exception("Location services (GPS/Network) are disabled.")
class MissingLocationPermissionException : Exception("Location permission is missing.")