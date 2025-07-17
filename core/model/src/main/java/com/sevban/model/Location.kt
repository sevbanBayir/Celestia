package com.sevban.model

typealias DomainLocation = Location

data class Location(
    val latitude: Double,
    val longitude: Double,
    val locationType: LocationType? = null
)

sealed interface LocationType {
    data object Current : LocationType
    data class LastKnown(val timestamp: Long) : LocationType
}
