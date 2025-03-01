package com.sevban.detail

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sevban.ui.model.LocationArgument
import com.sevban.ui.model.locationArgumentNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

fun NavController.navigateToDetail(
    location: LocationArgument
) {
    navigate(Detail(location))
}

fun NavGraphBuilder.detailScreen(
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
) {
    composable<Detail>(
        typeMap = Detail.typeMap
    ) {
        val viewModel: DetailViewModel = hiltViewModel()
        val weatherState by viewModel.forecastState.collectAsStateWithLifecycle()

        DetailScreen(
            forecastState = weatherState,
            onEvent = viewModel::onEvent,
            whenErrorOccurred = whenErrorOccurred,
        )
    }
}

@Serializable
data class Detail(val location: LocationArgument) {
    companion object {
        val typeMap = mapOf(typeOf<LocationArgument>() to locationArgumentNavType)
    }
}
