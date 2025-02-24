package com.sevban.location.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.location.R
import com.sevban.location.model.PlaceListState
import com.sevban.model.Place
import com.sevban.ui.components.LoadingScreen

@Composable
fun PlaceList(
    modifier: Modifier = Modifier,
    placeListState: PlaceListState,
    isPlaceListLoading: Boolean,
    onPlaceClick: (Place) -> Unit
) {
    val shape = RoundedCornerShape(
        bottomStart = 12.dp,
        bottomEnd = 12.dp,
        topStart = 0.dp,
        topEnd = 0.dp
    )

    val shapeModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 17.dp)
        .clip(shape)
        .background(
            MaterialTheme.colorScheme.background,
            shape
        )

    val transitionSpec: (targetState: Boolean) -> ContentTransform = { targetState ->
        if (targetState) {
            slideInVertically { it } togetherWith slideOutVertically { -it }
        } else {
            slideInVertically(tween()) { -it } togetherWith slideOutVertically { it }
        }
    }

    AnimatedContent(
        targetState = isPlaceListLoading,
        modifier = modifier,
        transitionSpec = { transitionSpec(targetState) }
    ) {
        when {
            it -> {
                Box(
                    modifier = shapeModifier
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingScreen()
                }
            }

            placeListState is PlaceListState.Empty -> {
                Box(
                    modifier = shapeModifier
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.map_tf_no_results),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            placeListState is PlaceListState.Success -> {
                LazyColumn(modifier = shapeModifier) {
                    items(placeListState.places) { place ->
                        PlaceListItem(
                            place = place,
                            onPlaceClick = onPlaceClick,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            placeListState is PlaceListState.Initial -> Unit
        }
    }
}
