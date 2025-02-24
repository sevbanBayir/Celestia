package com.sevban.location

import com.sevban.model.Place

sealed interface LocationScreenEvent {
    data class OnSearchQueryChanged(val query: String) : LocationScreenEvent
    data class OnLocationSelected(val prediction: Place) : LocationScreenEvent
}