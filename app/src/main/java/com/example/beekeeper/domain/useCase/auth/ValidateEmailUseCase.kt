package com.example.beekeeper.domain.usecase.auth

import com.example.beekeeper.domain.utils.EmailValidator
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {

    operator fun invoke(email:String):Boolean{
        return email.matches(EmailValidator.emailRegex)
    }

}