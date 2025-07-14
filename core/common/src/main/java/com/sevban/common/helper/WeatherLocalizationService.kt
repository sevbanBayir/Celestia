package com.sevban.common.helper

import android.content.Context
import com.sevban.common.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherLocalizationService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getPressureStatus(pressure: Int?): String {
        return when {
            pressure == null -> ""
            pressure < 1000 -> context.getString(R.string.pressure_low)
            pressure > 1020 -> context.getString(R.string.pressure_high)
            else -> context.getString(R.string.pressure_normal)
        }
    }

    fun formatTempRange(min: Double?, max: Double?): String {
        if (min == null || max == null) return ""
        return context.getString(
            R.string.temp_range_format,
            min.toInt().toString(),
            max.toInt().toString()
        )
    }

    fun formatPressureWithUnit(pressure: Int?): String {
        return if (pressure != null) {
            context.getString(R.string.pressure_with_unit, pressure.toString())
        } else ""
    }

    fun formatRainInfo(rainVolume1h: Double?): String {
        return when {
            rainVolume1h == null || rainVolume1h <= 0.0 -> context.getString(R.string.no_rain)
            rainVolume1h < 2.5 -> context.getString(R.string.light_rain)
            rainVolume1h < 10.0 -> context.getString(R.string.moderate_rain)
            else -> context.getString(R.string.heavy_rain)
        }
    }

    fun formatRainVolume(rainVolume1h: Double?): String {
        return if (rainVolume1h != null && rainVolume1h > 0.0) {
            context.getString(R.string.rain_volume_format, rainVolume1h.toString())
        } else ""
    }

    fun formatPrecipitationChance(probability: Double?): String {
        return if (probability != null) {
            val percentage = (probability * 100).toInt()
            context.getString(R.string.precipitation_chance_format, percentage.toString())
        } else ""
    }

    fun isRaining(rainVolume1h: Double?): Boolean {
        return rainVolume1h != null && rainVolume1h > 0.0
    }
} 