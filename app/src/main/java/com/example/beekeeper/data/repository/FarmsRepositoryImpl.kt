package com.example.beekeeper.data.repository

import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.data.common.mapResource
import com.example.beekeeper.data.source.remote.internet.mappers.toDomain
import com.example.beekeeper.data.source.remote.internet.service.FarmsService
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.Farm
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import kotlinx.coroutines.flow.Flow
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

    override fun getFarmDetails(id: Int): Flow<Resource<Farm>> {
        TODO("Not yet implemented")
    }

}