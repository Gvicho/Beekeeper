package com.example.beekeeper.data.repository

import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.data.common.mapResource
import com.example.beekeeper.data.source.remote.internet.mappers.farms.details.toDomain
import com.example.beekeeper.data.source.remote.internet.mappers.farms.toDomain
import com.example.beekeeper.data.source.remote.internet.service.FarmsService
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.common.Result
import com.example.beekeeper.domain.error_handling.DataError
import com.example.beekeeper.domain.error_handling.getNetworkErrorFromCode
import com.example.beekeeper.domain.model.farms.Farm
import com.example.beekeeper.domain.model.farms.details.FarmDetails
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FarmsRepositoryImpl @Inject constructor(
    private val farmsService: FarmsService,
    private val handleResponse: HandleResponse
) :FarmsRepository{

    override fun getFarmsList(): Flow<Resource<List<Farm>>> {
        return handleResponse.safeApiCallRetrofit {
            farmsService.getFarms()
        }.mapResource {
            it.map {farm->
                farm.toDomain()
            }
        }
    }

    override fun getFarmDetails(id: Int): Flow<Result<FarmDetails,DataError.NetworkError>> {
        return flow {
            try {
                emit(Result.Loading())
                val response = farmsService.getFarmDetails(id)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    emit(Result.Success(responseData = body.toDomain()))
                } else {
                    emit(Result.Failed(error = getNetworkErrorFromCode(response.code())))
                }
            } catch (e: HttpException) {
                emit(Result.Failed(error  = getNetworkErrorFromCode(e.code())))
            } catch (e :IOException){
                emit(Result.Failed(error  = DataError.NetworkError.NO_INTERNET))
            }
        }
    }
}