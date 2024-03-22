package com.example.beekeeper.presenter.state.worker_states

data class WorkerStatusState(
    val isLoading:Boolean = false,
    val uploadedSuccessfully:Unit? = null,
    val failedMessage:String? = null,
    val blocked:Unit? = null,
    val wasCanceled:Unit? = null
)