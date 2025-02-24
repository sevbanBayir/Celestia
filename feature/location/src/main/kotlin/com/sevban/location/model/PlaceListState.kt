package com.sevban.location.model

import com.sevban.model.Place

sealed interface PlaceListState {
    data class Success(val places: List<Place>) : PlaceListState
    data object Empty : PlaceListState
    data object Initial : PlaceListState
}