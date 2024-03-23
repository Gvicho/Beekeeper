package com.example.beekeeper.presenter.model.drawer_menu

data class Option(val name: String, val type: Options, val icon: Int, var status: Boolean = false, ){
}

enum class Options {
    DARK_MODE,
    LANGUAGE,
    CHANGE_PASSWORD,
    LOG_OUT,
    PROFILE,
}