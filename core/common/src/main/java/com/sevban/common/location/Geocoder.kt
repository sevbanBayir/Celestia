package com.sevban.common.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build

import com.sevban.common.helper.DispatcherProvider
import com.sevban.model.Place
import com.sevban.model.PlaceText
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class Geocoder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) {
    private val geocoder = Geocoder(context)

    suspend fun getPlace(latitude: Double, longitude: Double): Place? =
        withContext(dispatcherProvider.defaultDispatcher) {
            require(Geocoder.isPresent()) { "Geocoder is not present" }

            val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Use new async API for Android 13+ (API 33+)
                suspendCancellableCoroutine<List<Address>?> { continuation ->
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        continuation.resume(addresses)
                    }
                }
            } else {
                // Fallback to deprecated sync API for older versions
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latitude, longitude, 1)
            }

            addresses?.firstOrNull()?.let { address ->
                val cityName = address.locality
                    ?: address.subLocality
                    ?: address.subAdminArea
                    ?: address.adminArea

                val countryName = address.countryName

                Place(
                    cityName = cityName,
                    country = countryName,
                    latitude = address.latitude,
                    longitude = address.longitude,
                    fullText = "$cityName, $countryName"
                )
            }
        }

    suspend fun getPlaceCoordinates(placeText: PlaceText): Place? =
        withContext(dispatcherProvider.defaultDispatcher) {
            require(Geocoder.isPresent()) { "Geocoder is not present" }

            val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Use new async API for Android 13+ (API 33+)
                suspendCancellableCoroutine<List<Address>?> { continuation ->
                    geocoder.getFromLocationName(placeText.predictedFullText, 1) { addresses ->
                        continuation.resume(addresses)
                    }
                }
            } else {
                // Fallback to deprecated sync API for older versions
                @Suppress("DEPRECATION")
                geocoder.getFromLocationName(placeText.predictedFullText, 1)
            }

            addresses?.firstOrNull()?.let { address ->
                Place(
                    cityName = placeText.predictedCityText,
                    country = placeText.predictedCountryText,
                    fullText = placeText.predictedFullText,
                    latitude = address.latitude,
                    longitude = address.longitude
                )
            }
        }
}
