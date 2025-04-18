package com.sevban.common.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.sevban.common.helper.DispatcherProvider
import com.sevban.model.Place
import com.sevban.model.PlaceText
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
                println(address)
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

    suspend fun getCityCoordinates(placeText: PlaceText): Place? {
        // Check for API level >= 33 for the new listener API
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getCityCoordinatesWithListener(placeText)
        } else {
            // Fallback to the deprecated (but still functional) method on older APIs
            // Ensure this runs on a background thread
            getCityCoordinatesDeprecated(placeText)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun getCityCoordinatesWithListener(placeText: PlaceText): Place? =
        // No need for withContext here, the listener handles asynchronicity
        suspendCancellableCoroutine { continuation ->
            require(Geocoder.isPresent()) { "Geocoder is not present" }

            val listener = object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (continuation.isActive) { // Check if coroutine is still active
                        val address = addresses.firstOrNull()
                        val place = address?.toPlace() // Use helper function
                        continuation.resume(place)
                    }
                }

                override fun onError(errorMessage: String?) {
                    if (continuation.isActive) {
                         println("Geocoder error: $errorMessage")
                         // Decide if you want to resume with null or throw an exception
                         // continuation.resume(null)
                         continuation.resumeWithException(Exception("Geocoder failed: $errorMessage"))
                    }
                }
            }

            try {
                 geocoder.getFromLocationName(placeText.fullText, 1, listener)
            } catch (e: Exception) { // Catch immediate exceptions like IllegalArgumentException
                 if (continuation.isActive) {
                    println("Error calling getFromLocationName: $e")
                    continuation.resumeWithException(e)
                 }
            }

            // Handle coroutine cancellation
            continuation.invokeOnCancellation {
                // Optional: Cancel any ongoing geocoder work if possible,
                // although the standard Geocoder doesn't offer a direct cancel method.
                println("Geocoding coroutine cancelled for: ${placeText.fullText}")
            }
        }

    // Keep the old method for devices below API 33
    @Suppress("DEPRECATION")
    private suspend fun getCityCoordinatesDeprecated(placeText: PlaceText): Place? =
        withContext(dispatcherProvider.ioDispatcher) { // Ensure it runs on IO dispatcher
            require(Geocoder.isPresent()) { "Geocoder is not present" }

            val addresses = try {
                // The deprecated synchronous call
                geocoder.getFromLocationName(placeText.fullText, 1)
            } catch (e: Exception) {
                println("error in getCityCoordinatesDeprecated: $e")
                null
            }
            addresses?.firstOrNull()?.toPlace() // Use helper function
        }

    // Helper function to convert Address to Place
    private fun Address.toPlace(): Place? {
        // Check for valid lat/lon, as they are required by your Place model
        if (!hasLatitude() || !hasLongitude()) {
             println("Address for '${this.featureName}' missing lat/lon.")
             return null
        }
        return Place(
            cityName = this.locality ?: this.subLocality ?: this.subAdminArea ?: this.adminArea,
            country = this.countryName,
            latitude = this.latitude,
            longitude = this.longitude
        )
    }
}
