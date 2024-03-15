package com.example.beekeeper.domain.usecase.farms

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.farms.details.FarmDetails
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmDetailsByIdUseCase@Inject constructor(
    private val farmsRepository: FarmsRepository
) {
    operator fun invoke(id:Int): Flow<Resource<FarmDetails>> = farmsRepository.getFarmDetails(id)
}