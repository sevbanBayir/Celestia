package com.sevban.common.helper

import android.content.Context
import com.sevban.common.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
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
            context.getString(R.string.precipitation_chance_format, (probability * 100).toInt().toString())
        } else ""
    }

    fun isRaining(rainVolume1h: Double?): Boolean {
        return rainVolume1h != null && rainVolume1h > 0.0
    }

    /**
     * Gets localized day of week name (e.g., "Pazartesi" for Monday in Turkish)
     */
    fun getLocalizedDayOfWeek(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    /**
     * Gets localized short day of week name (e.g., "Pzt" for Monday in Turkish)
     */
    fun getLocalizedShortDayOfWeek(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    /**
     * Gets localized day and month (e.g., "15 Ocak" in Turkish)
     */
    fun getLocalizedDayAndMonth(date: LocalDate): String {
        val dayOfMonth = date.dayOfMonth
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "$dayOfMonth $month"
    }
} 