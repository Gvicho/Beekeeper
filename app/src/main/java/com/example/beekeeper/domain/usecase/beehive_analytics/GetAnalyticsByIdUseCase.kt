package com.example.beekeeper.domain.usecase.beehive_analytics

import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import javax.inject.Inject

class GetAnalyticsByIdUseCase @Inject constructor(private val beehiveAnalyticsRepository: BeehiveAnalyticsRepository) {

    suspend operator fun invoke(id: Int) =
        beehiveAnalyticsRepository.getAnalyticsByIdFromLocal(id)

}