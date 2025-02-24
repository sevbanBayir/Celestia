package com.sevban.home

sealed interface HomeScreenEvent {
    data object OnLocationPermissionDeclined : HomeScreenEvent
    data object OnLocationPermissionPermanentlyDeclined : HomeScreenEvent
    data object OnPermissionDialogDismissed : HomeScreenEvent
    data object OnTryAgainClick : HomeScreenEvent
}