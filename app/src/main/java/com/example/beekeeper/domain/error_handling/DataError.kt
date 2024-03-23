package com.example.beekeeper.domain.error_handling

sealed interface DataError:Error {

    enum class NetworkError:Error{
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERVER_ERROR,
        BAD_REQUEST,
        NOT_FOUND,
        OTHER_NETWORK_ERROR
    }



}

fun getNetworkErrorFromCode(code: Int): DataError.NetworkError {
    return when (code) {
        400 -> DataError.NetworkError.BAD_REQUEST
        404 -> DataError.NetworkError.NOT_FOUND
        in 408..499 -> DataError.NetworkError.REQUEST_TIMEOUT
        in 500..599 -> DataError.NetworkError.SERVER_ERROR
        else -> DataError.NetworkError.OTHER_NETWORK_ERROR
    }
}