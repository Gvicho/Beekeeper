package com.example.beekeeper.domain.usecase.beehive_analytics

import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import javax.inject.Inject

class DeleteAllAnalyticsUseCase  @Inject constructor(private val beehiveAnalyticsRepository: BeehiveAnalyticsRepository) {

    operator fun invoke() =
        beehiveAnalyticsRepository.deleteAllFromLocal()

}