package com.example.beekeeper.presenter.state.damage_report.imagesList

import android.net.Uri

data class ImagesListState(
    val images: List<Uri> = emptyList()
)