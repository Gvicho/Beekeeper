package com.example.beekeeper.data.common

sealed class Resource<out D: Any>(
) {

    data class Success<out D: Any>(val responseData: D) : Resource<D>()
    data class Failed<out D: Any>(val message: String) : Resource<D>()
    class Loading<Nothing: Any>() : Resource<Nothing>()

}