package com.sevban.detail

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

fun NavController.navigateToDetail(
    latitude: Double,
    longitude: Double,
) {
    navigate(Detail(latitude, longitude)) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.detailScreen(
    whenErrorOccured: suspend (Throwable, String?) -> Unit,
) {
    composable<Detail> {
        val viewModel: DetailViewModel = hiltViewModel()
        val weatherState by viewModel.forecastState.collectAsStateWithLifecycle()

        DetailScreen(
            forecastState = weatherState,
            onEvent = viewModel::onEvent,
            whenErrorOccurred = whenErrorOccured,
        )
    }
}

@Serializable
data class Detail(val latitude: Double, val longitude: Double)

