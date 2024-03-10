package com.example.beekeeper.domain.usecase.beehive_analytics

import com.example.beekeeper.domain.model.BeehiveAnalytics
import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import javax.inject.Inject

class InsertAnalyticsUseCase @Inject constructor(private val beehiveAnalyticsRepository: BeehiveAnalyticsRepository) {

    operator fun invoke(analytics: BeehiveAnalytics) =
        beehiveAnalyticsRepository.insertAnalyticsInLocal(analytics)

}