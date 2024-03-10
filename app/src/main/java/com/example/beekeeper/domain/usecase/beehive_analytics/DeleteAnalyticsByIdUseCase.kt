package com.example.beekeeper.domain.usecase.beehive_analytics

import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import javax.inject.Inject

class DeleteAnalyticsByIdUseCase @Inject constructor(private val beehiveAnalyticsRepository: BeehiveAnalyticsRepository) {

    operator fun invoke(id:Int) =
        beehiveAnalyticsRepository.deleteAnalyticsByIdFromLocal(id)

}