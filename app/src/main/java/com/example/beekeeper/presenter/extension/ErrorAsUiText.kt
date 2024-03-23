package com.example.beekeeper.presenter.extension

import com.example.beekeeper.R
import com.example.beekeeper.domain.error_handling.DataError


fun DataError.NetworkError.asUiText():Int{
    return when(this){
        DataError.NetworkError.REQUEST_TIMEOUT -> R.string.REQUEST_TIMEOUT
        DataError.NetworkError.NO_INTERNET -> R.string.NO_INTERNET
        DataError.NetworkError.SERVER_ERROR -> R.string.SERVER_ERROR
        DataError.NetworkError.BAD_REQUEST -> R.string.BAD_REQUEST
        DataError.NetworkError.NOT_FOUND -> R.string.NOT_FOUND
        DataError.NetworkError.OTHER_NETWORK_ERROR -> R.string.OTHER_NETWORK_ERROR
    }
}