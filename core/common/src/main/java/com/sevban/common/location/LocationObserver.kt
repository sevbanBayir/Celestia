package com.sevban.common.location

import com.sevban.model.DomainLocation
import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun getCurrentLocation(): Flow<DomainLocation?>
    fun getLastKnownLocation(): Flow<DomainLocation?>
}