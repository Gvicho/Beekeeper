package com.example.beekeeper.presenter.extension

import android.util.Log
import androidx.navigation.NavController

fun NavController.safeNavigate(actionId:Int) {
    val currentDestinationId = currentDestination?.id
    Log.d("Navigation", "Current destination ID: $currentDestinationId")
    currentDestination?.getAction(actionId)?.run {
        navigate(actionId)
    } ?: Log.w("Navigation", "Cannot find action ID $actionId from current destination $currentDestinationId")
}