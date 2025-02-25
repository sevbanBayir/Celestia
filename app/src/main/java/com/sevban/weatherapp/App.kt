package com.sevban.weatherapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sevban.weatherapp.navigation.WeatherAppNavHost

@Composable
fun App(
    appState: AppState = rememberAppState()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        WeatherAppNavHost(
            appState = appState,
            onShowSnackbar = { failure, message ->
                snackbarHostState.showSnackbar(message ?: failure.toString())
            },
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
                .statusBarsPadding()
        )
    }
}