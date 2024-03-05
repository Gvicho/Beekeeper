package com.example.beekeeper.data.common


import com.example.beekeeper.domain.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <Dto:Any, DomainEntity :Any> Flow<Resource<Dto>>.mapResource(mapper: (Dto) -> DomainEntity):Flow<Resource<DomainEntity>>{
    return this.map{ result ->
        when(result){
            is Resource.Success ->{
                Resource.Success(responseData = mapper(result.responseData)) // this will never be null because in HandleResponse we never let null body response be wrapped in successful
            }
            is Resource.Failed -> {
                Resource.Failed(message = result.message)
            }
            is Resource.Loading ->{
                Resource.Loading()
            }
        }
    }
}