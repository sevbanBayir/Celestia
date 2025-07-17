package com.sevban.common.location.mapper

import android.location.Location
import com.sevban.model.DomainLocation
import com.sevban.model.LocationType

fun Location.toDomainLocation(locationType: LocationType? = null): DomainLocation {
    return DomainLocation(
        latitude = latitude,
        longitude = longitude,
        locationType = locationType
    )
}