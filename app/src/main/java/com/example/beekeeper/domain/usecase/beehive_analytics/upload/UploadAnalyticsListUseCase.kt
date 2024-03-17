package com.example.beekeeper.domain.usecase.beehive_analytics.upload

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsUploadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadAnalyticsListUseCase @Inject constructor(
    private val uploadRepository: BeehiveAnalyticsUploadRepository
) {

    operator fun invoke(analyticsList:List<BeehiveAnalytics>): Flow<Resource<Unit>> = uploadRepository.uploadBeehiveAnalytics(analyticsList)

}