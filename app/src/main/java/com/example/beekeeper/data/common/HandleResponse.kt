package com.example.beekeeper.data.common
import android.util.Log
import com.example.beekeeper.data.common.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.flow

class HandleResponse {

    suspend fun  safeApiCall(call: suspend () -> AuthResult) = flow<Resource<AuthResult>> {
        emit(Resource.Loading())
        try {

            val response = call()

            emit(Resource.Success(response))

        }catch (e: FirebaseAuthException){
            emit(Resource.Failed(handleFirebaseAuthException(e)))

        }
        catch (e: Exception) {
            emit(Resource.Failed("Failed"))
        }
    }
    private fun handleFirebaseAuthException(e: FirebaseAuthException): String {
        when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                return "Email already in use"
            }
            "ERROR_INVALID_EMAIL" -> {
               return "Invalid Email"
            }
            "ERROR_USER_NOT_FOUND", "ERROR_WRONG_PASSWORD" -> {
                return "Incorrect Email or password"
            }
            // Handle other FirebaseAuthException error codes as needed
            else -> {
                // Show a generic error message or take action
                return "Failed"

            }
        }
    }

}
