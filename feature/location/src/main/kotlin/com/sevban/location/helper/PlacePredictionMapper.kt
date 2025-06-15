package com.sevban.location.helper

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.sevban.model.PlaceText

fun AutocompletePrediction.toPlace(): PlaceText {
    val fullText = getFullText(null)
    val primaryText = getPrimaryText(null)
    val secondaryText = getSecondaryText(null)

    val predictedPrimaryText = primaryText.toString() // City
    val predictedSecondaryText = secondaryText.toString() // Country

    return PlaceText(
        predictedFullText = fullText.toString(),
        predictedCityText = predictedPrimaryText,
        predictedCountryText = predictedSecondaryText
    )
}