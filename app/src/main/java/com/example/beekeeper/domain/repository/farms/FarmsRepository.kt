package com.example.beekeeper.domain.repository.farms

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.common.Result
import com.example.beekeeper.domain.error_handling.DataError
import com.example.beekeeper.domain.model.farms.Farm
import com.example.beekeeper.domain.model.farms.details.FarmDetails
import kotlinx.coroutines.flow.Flow

interface FarmsRepository {

    fun getFarmsList(): Flow<Resource<List<Farm>>>

    fun getFarmDetails(id:Int):Flow<Result<FarmDetails, DataError.NetworkError>>  // farm details are not created yet

}