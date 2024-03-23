package com.example.beekeeper.domain.usecase.farms

import com.example.beekeeper.domain.common.Result
import com.example.beekeeper.domain.error_handling.DataError
import com.example.beekeeper.domain.model.farms.details.FarmDetails
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmDetailsByIdUseCase@Inject constructor(
    private val farmsRepository: FarmsRepository
) {
    operator fun invoke(id:Int): Flow<Result<FarmDetails, DataError.NetworkError>> = farmsRepository.getFarmDetails(id)
}