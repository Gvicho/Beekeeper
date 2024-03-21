package com.example.beekeeper.domain.usecase.user

import javax.inject.Inject

class ValidateNameAndLastNameUseCase @Inject constructor() {

    operator fun invoke(name:String, lastName:String):Boolean{
        return name.isNotEmpty() && lastName.isNotEmpty()
    }

}