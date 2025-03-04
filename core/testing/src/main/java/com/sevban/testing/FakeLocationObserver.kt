package com.sevban.testing

import com.sevban.common.location.LocationObserver
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.model.DomainLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FakeLocationObserver : LocationObserver {

    var shouldThrowPermissionException = false
    var currentLocationCallTimes = 0

    override fun getCurrentLocation(): Flow<DomainLocation> = callbackFlow {
        currentLocationCallTimes++
        if (shouldThrowPermissionException) {
            close(MissingLocationPermissionException())
        } else {
            trySend(DomainLocation(4.0, 3.0))
        }
        awaitClose()
    }

}