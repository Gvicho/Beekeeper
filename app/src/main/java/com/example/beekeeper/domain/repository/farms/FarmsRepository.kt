package com.example.beekeeper.domain.repository.farms

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.farms.Farm
import kotlinx.coroutines.flow.Flow

interface FarmsRepository {

    fun getFarmsList(): Flow<Resource<List<Farm>>>

    fun getFarmDetails(id:Int): Flow<Resource<Farm>>  // farm details are not created yet

}