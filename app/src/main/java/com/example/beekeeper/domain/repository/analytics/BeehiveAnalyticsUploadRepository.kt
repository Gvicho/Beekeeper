package com.example.beekeeper.domain.repository.analytics

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import kotlinx.coroutines.flow.Flow

interface BeehiveAnalyticsUploadRepository {
    fun uploadBeehiveAnalytics(analyticsList:List<BeehiveAnalytics>): Flow<Resource<Unit>>
}