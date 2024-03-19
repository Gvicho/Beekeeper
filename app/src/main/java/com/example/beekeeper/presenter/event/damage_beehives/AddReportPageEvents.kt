package com.example.beekeeper.presenter.event.damage_beehives

import android.net.Uri

sealed class AddReportPageEvents {

    data class UploadReport(
        val desc: String,
        val damageLevel: Int,
        val uris: List<Uri>
    ):AddReportPageEvents()

    data class GetDescription(val images: List<Uri>):AddReportPageEvents()
    data object ResetErrorMessageOfUploadToNull:AddReportPageEvents()
    data object ResetErrorMessageOfDescriptionToNull:AddReportPageEvents()
}