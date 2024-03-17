package com.example.beekeeper.data.repository

import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.data.source.remote.internet.mappers.analytics.toDto
import com.example.beekeeper.data.source.remote.internet.service.FarmsService
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsUploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BeehiveAnalyticsUploadRepositoryImpl@Inject constructor(
    private val farmsService: FarmsService,
    private val handleResponse: HandleResponse
) : BeehiveAnalyticsUploadRepository{

    override fun uploadBeehiveAnalytics(analyticsList: List<BeehiveAnalytics>): Flow<Resource<Unit>> {
        return handleResponse.safeApiCallRetrofit {
            val analyticsDtoList = withContext(Dispatchers.IO){ analyticsList.map { it.toDto() }}
            farmsService.uploadAnalytics(analyticsDtoList)
        }
    }

}