package com.example.beekeeper.domain.common
import com.example.beekeeper.domain.error_handling.Error

typealias RootError = Error

sealed interface Result<out D,out E:RootError> {
    data class Success<out D: Any, out E:RootError>(val responseData: D) : Result<D,E>
    data class Failed<out D: Any, out E:RootError>(val error: E) : Result<D,E>
    class Loading<Nothing: Any, out Empty:RootError> : Result<Nothing,Empty>
}