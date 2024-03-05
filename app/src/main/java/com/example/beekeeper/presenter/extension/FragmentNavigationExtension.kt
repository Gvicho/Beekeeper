package com.example.beekeeper.presenter.extension

import androidx.navigation.NavController

fun NavController.safeNavigateWithArgs(actionId:Int) {
    currentDestination?.getAction(actionId)?.run {
        navigate(actionId)
    }
}