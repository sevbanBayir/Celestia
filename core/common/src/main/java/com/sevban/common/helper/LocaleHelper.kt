package com.sevban.common.helper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Gets the current device language for API calls.
     * Returns "tr" for Turkish, "en" for English (default).
     * OpenWeather API supports: en, tr, and many others
     */
    fun getApiLanguage(): String {
        val deviceLanguage = Locale.getDefault().language
        return when (deviceLanguage) {
            "tr" -> "tr"
            else -> "en" // Default to English for all other languages
        }
    }

    /**
     * Gets the full locale for additional formatting needs
     */
    fun getCurrentLocale(): Locale = Locale.getDefault()
} 