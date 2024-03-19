package com.example.beekeeper.domain.usecase.farms

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.farms.Farm
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFarmsListUseCase@Inject constructor(private val farmsRepository: FarmsRepository) {
    operator fun invoke(word:String): Flow<Resource<List<Farm>>> = farmsRepository.getFarmsList().map {
        when(it){
            is Resource.Failed -> it
            is Resource.Loading -> it
            is Resource.Success -> {
                it.copy( responseData = it.responseData.filter {farm->
                    farm.farmName.contains(word,ignoreCase = true)
                })
            }
        }
    }
}