package com.example.beekeeper.domain.repository.analytics

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import com.example.beekeeper.domain.model.analytics.SavedAnalyticsPartial
import kotlinx.coroutines.flow.Flow

interface BeehiveAnalyticsRepository {
    fun getPartialAnalyticsFromLocal(): Flow<Resource<List<SavedAnalyticsPartial>>>
    fun getAnalyticsByIdFromLocal(id: Int): Flow<Resource<BeehiveAnalytics>>
    fun deleteAllFromLocal(): Flow<Resource<Unit>>
    fun insertAnalyticsInLocal(analytics: BeehiveAnalytics): Flow<Resource<Long>>

    fun deleteAnalyticsByIdFromLocal(id: Int): Flow<Resource<Unit>>
}