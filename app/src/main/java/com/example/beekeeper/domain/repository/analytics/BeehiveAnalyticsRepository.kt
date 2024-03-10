package com.example.beekeeper.domain.repository.analytics

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.BeehiveAnalytics
import kotlinx.coroutines.flow.Flow

interface BeehiveAnalyticsRepository {

    suspend fun getAnalyticsFromLocal(): Flow<Resource<List<BeehiveAnalytics>>>
    suspend fun getAnalyticsByIdFromLocal(id: Int): Flow<Resource<BeehiveAnalytics>>
    suspend fun deleteAllFromLocal(): Flow<Resource<Boolean>>
    suspend fun insertAnalyticsInLocal(analytics:BeehiveAnalytics): Flow<Resource<Long>>
}