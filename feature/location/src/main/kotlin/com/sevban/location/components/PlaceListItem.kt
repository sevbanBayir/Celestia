package com.sevban.location.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.sevban.model.Place

@Composable
fun PlaceListItem(
    place: Place,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    val countryColor =
        if (place.cityName == null) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
            alpha = 0.5f
        )

    Card(
        modifier = modifier.heightIn(min = 56.dp),
        onClick = { onPlaceClick(place) },
        shape = RectangleShape
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.heightIn(min = 56.dp),
        ) {

            Text(
                text = place.cityName ?: place.country,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            if (place.cityName != null)
                Text(
                    text = place.country,
                    color = countryColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 0.dp,
                            bottom = 4.dp
                        )
                )
        }

    }
}