package com.example.beekeeper.presenter.extension

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(action: NavDirections) {
    currentDestination?.getAction(action.actionId)?.run {
        navigate(action)
    }
}