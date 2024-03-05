package com.example.beekeeper.data.common
import com.example.beekeeper.domain.common.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

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

    fun <T : Any> safeApiCallRetrofit(call: suspend () -> Response<T>): Flow<Resource<T>> = flow {
        try {
            emit(Resource.Loading())
            val response = call()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(Resource.Success(responseData = body))
            } else {
                emit(Resource.Failed(message = response.errorBody()?.string() ?: ""))
            }
        } catch (e: Throwable) {
            emit(Resource.Failed(message  = e.message ?: ""))
        }
    }

}
