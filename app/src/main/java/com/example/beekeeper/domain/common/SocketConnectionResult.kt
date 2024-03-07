package com.example.beekeeper.domain.common

sealed interface SocketConnectionResult {
    data object ConnectionEstablished:SocketConnectionResult
    data class Error(val errorMessage:String): SocketConnectionResult
}