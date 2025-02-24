package com.sevban.location.helper

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.sevban.model.PlaceText

fun AutocompletePrediction.toPlace(): PlaceText {
    val fullText = getFullText(null)
    val primaryText = getPrimaryText(null)
    val secondaryText = getSecondaryText(null)

    val name = primaryText.toString()
    val country = secondaryText.toString()

    return PlaceText(
        fullText = fullText.toString(),
        primaryText = name,
        secondaryText = country
    )
}