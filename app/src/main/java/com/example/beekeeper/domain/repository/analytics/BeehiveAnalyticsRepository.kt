package com.example.beekeeper.domain.repository.analytics

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.BeehiveAnalytics
import kotlinx.coroutines.flow.Flow

interface BeehiveAnalyticsRepository {
    fun getAnalyticsFromLocal(): Flow<Resource<List<BeehiveAnalytics>>>
    fun getAnalyticsByIdFromLocal(id: Int): Flow<Resource<BeehiveAnalytics>>
    fun deleteAllFromLocal(): Flow<Resource<Boolean>>
    fun insertAnalyticsInLocal(analytics: BeehiveAnalytics): Flow<Resource<Long>>

    fun deleteAnalyticsByIdFromLocal(id: Int): Flow<Resource<Boolean>>
}