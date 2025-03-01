package com.sevban.common.location

import android.content.Context
import android.location.Geocoder
import com.sevban.common.helper.DispatcherProvider
import com.sevban.model.Place
import com.sevban.model.PlaceText
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Geocoder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) {
    private val geocoder = Geocoder(context)

    suspend fun getPlace(latitude: Double, longitude: Double): Place? =
        withContext(dispatcherProvider.defaultDispatcher) {
            require(Geocoder.isPresent()) { "Geocoder is not present" }

            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.let { address ->
                Place(
                    cityName = address.locality ?: address.subLocality ?: address.subAdminArea ?: address.adminArea,
                    country = address.countryName,
                    latitude = address.latitude,
                    longitude = address.longitude
                )
            }
        }

    suspend fun getPlaceCoordinates(placeText: PlaceText): Place? =
        withContext(dispatcherProvider.defaultDispatcher) {
            require(Geocoder.isPresent()) { "Geocoder is not present" }

            val addresses = geocoder.getFromLocationName(placeText.fullText, 1)
            addresses?.firstOrNull()?.let { address ->
                Place(
                    cityName = placeText.primaryText,
                    country = placeText.secondaryText,
                    latitude = address.latitude,
                    longitude = address.longitude
                )
            }
        }

    suspend fun getCityCoordinates(placeText: PlaceText): Place? =
        withContext(dispatcherProvider.defaultDispatcher) {
            require(Geocoder.isPresent()) { "Geocoder is not present" }

            // Todo: If "gj" text comes from this api, an exception thrown ???
            val addresses = geocoder.getFromLocationName(placeText.fullText, 1)
            addresses?.firstOrNull()?.let { address ->
                Place(
                    cityName = address.locality ?: address.subLocality ?: address.subAdminArea ?: address.adminArea,
                    country = address.countryName,
                    latitude = address.latitude,
                    longitude = address.longitude
                )
            }
        }

}
