package com.example.beekeeper.domain.usecase.auth

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    operator fun invoke(password:String):Boolean{
        return password.length>=6 && password.any { it.isDigit() }
    }

}