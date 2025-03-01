package com.sevban.ui.model

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.sevban.model.DomainLocation
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class LocationArgument(
    val latitude: Double,
    val longitude: Double
) : Parcelable

fun DomainLocation.toLocationArgument() = LocationArgument(
    latitude = latitude,
    longitude = longitude
)

val locationArgumentNavType = object : NavType<LocationArgument?>(
    isNullableAllowed = true
) {
    override fun get(bundle: Bundle, key: String): LocationArgument? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, LocationArgument::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): LocationArgument {
        return Json.decodeFromString<LocationArgument>(value)
    }

    override fun put(bundle: Bundle, key: String, value: LocationArgument?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: LocationArgument?): String {
        return Uri.encode(Json.encodeToString(value))
    }
}
