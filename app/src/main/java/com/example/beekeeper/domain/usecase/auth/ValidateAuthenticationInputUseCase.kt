package com.example.beekeeper.domain.usecase.auth

import javax.inject.Inject

class ValidateAuthenticationInputUseCase @Inject constructor(
    private val emailValidator: ValidateEmailUseCase,
    private val passwordValidator: ValidatePasswordUseCase
) {

    operator fun invoke(email:String,password:String):Boolean{
        return emailValidator(email) && passwordValidator(password)
    }

}