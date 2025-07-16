package com.sevban.common.model

import android.content.Context
import com.sevban.common.R

data class Failure(val errorType: ErrorType) : Exception() {
    constructor(throwable: Throwable) : this(errorType = ErrorType.UNKNOWN) {
        throwable.printStackTrace()
    }
}

enum class ErrorType {
    TOO_MANY_REQUESTS,
    SERVER_ERROR,
    UNKNOWN,
    NOT_FOUND,
    FORBIDDEN,
    UNAUTHORIZED,
    BAD_REQUEST,
    EMPTY_RESPONSE,
    SERIALIZATION,
    LOCATION_ERROR,
    CONNECTIVITY_ERROR,
    NO_DATA_AVAILABLE
}

fun ErrorType.toLocalizedMessage(context: Context) = when (this) {
    ErrorType.TOO_MANY_REQUESTS -> context.getString(R.string.made_too_many_requests)
    ErrorType.SERVER_ERROR -> context.getString(R.string.server_error)
    ErrorType.UNKNOWN -> context.getString(R.string.unknown_error)
    ErrorType.NOT_FOUND -> context.getString(R.string.not_found)
    ErrorType.FORBIDDEN -> context.getString(R.string.forbidden)
    ErrorType.UNAUTHORIZED -> context.getString(R.string.unauthorized)
    ErrorType.BAD_REQUEST -> context.getString(R.string.bad_request)
    ErrorType.EMPTY_RESPONSE -> context.getString(R.string.empty_response)
    ErrorType.SERIALIZATION -> context.getString(R.string.serialization_error)
    ErrorType.LOCATION_ERROR -> context.getString(R.string.location_error)
    ErrorType.CONNECTIVITY_ERROR -> context.getString(R.string.connectivity_error)
    ErrorType.NO_DATA_AVAILABLE -> context.getString(R.string.no_data_available)
}