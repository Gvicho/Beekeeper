package com.example.beekeeper.domain.usecase.damage_report

import android.net.Uri
import javax.inject.Inject

class ValidateUrisUseCase @Inject constructor() {
    operator  fun invoke(uris: List<Uri>): Boolean{
        return uris.isEmpty()
    }
}