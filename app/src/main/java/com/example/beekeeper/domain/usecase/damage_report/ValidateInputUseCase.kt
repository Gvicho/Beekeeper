package com.example.beekeeper.domain.usecase.damage_report

import javax.inject.Inject

class ValidateInputUseCase@Inject constructor() {

    operator  fun invoke(input: String?): Boolean{
        return input.isNullOrEmpty()
    }
}