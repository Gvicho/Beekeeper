package com.example.beekeeper.presenter.event.damage_beehives

import android.net.Uri

sealed class AddReportPageEvents {

    data class UploadReport(
        val desc: String,
        val damageLevel: Int
    ):AddReportPageEvents()

    data class AddImagesToList(val images:List<Uri>):AddReportPageEvents()

    data class RemoveImageFromList(val uri:Uri):AddReportPageEvents()

    data object GetDescription:AddReportPageEvents()
    data object ResetErrorMessageOfDescriptionToNull:AddReportPageEvents()
    data object ResetBlockToNull: AddReportPageEvents()
    data object ResetFailToNull: AddReportPageEvents()
    data object ResetSuccessToNull: AddReportPageEvents()
    data object ResetCancelToNull: AddReportPageEvents()
}