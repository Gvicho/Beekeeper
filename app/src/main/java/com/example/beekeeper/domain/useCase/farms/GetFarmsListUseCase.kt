package com.example.beekeeper.domain.usecase.farms

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.Farm
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmsListUseCase@Inject constructor(private val farmsRepository: FarmsRepository) {
    operator fun invoke(): Flow<Resource<List<Farm>>> = farmsRepository.getFarmsList()
}