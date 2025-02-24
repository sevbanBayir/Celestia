package com.sevban.location.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.location.model.PlaceListState
import com.sevban.model.Place

@Composable
fun SearchbarWithList(
    searchQuery: String,
    placeListState: PlaceListState,
    onSearchQueryChanged: (String) -> Unit,
    onPlaceClick: (Place) -> Unit,
    isPlaceListLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 0.dp
                )
        )

        PlaceList(
            placeListState = placeListState,
            onPlaceClick = onPlaceClick,
            isPlaceListLoading = isPlaceListLoading
        )
    }
}