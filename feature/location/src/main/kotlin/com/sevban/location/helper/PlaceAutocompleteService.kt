package com.sevban.location.helper

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.sevban.common.helper.DispatcherProvider
import com.sevban.location.BuildConfig
import com.sevban.model.PlaceText
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.properties.Delegates

class PlaceAutocompleteService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) {
    private val apiKey = BuildConfig.MAPS_API_KEY
    private var client: PlacesClient by Delegates.notNull()

    init {
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(context, apiKey)
        }
        client = Places.createClient(context)
    }

    fun getAutocomplete(query: String) = callbackFlow<List<PlaceText>> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
        val task = client.findAutocompletePredictions(request)

        task.addOnSuccessListener { response ->
            trySend(response.autocompletePredictions.map { it.toPlace() })
        }
        task.addOnFailureListener { exception ->
            trySend(emptyList())
            close(exception)
        }
        awaitClose()
    }.flowOn(dispatcherProvider.ioDispatcher)
}