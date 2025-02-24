package com.sevban.detail

sealed interface DetailScreenEvent {
    data object OnTryAgainClick : DetailScreenEvent
}