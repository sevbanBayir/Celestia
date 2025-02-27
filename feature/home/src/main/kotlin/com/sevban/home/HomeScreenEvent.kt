package com.sevban.home

sealed interface HomeScreenEvent {
    data object OnLocationPermissionDeclined : HomeScreenEvent
    data object OnGiveLocationPermissionClick : HomeScreenEvent
    data object OnLocationPermissionGranted : HomeScreenEvent
    data object OnLocationPermissionPermanentlyDeclined : HomeScreenEvent
    data object OnPermanentlyDeclinedDialogDismissed : HomeScreenEvent
    data object OnTryAgainClick : HomeScreenEvent
}