package com.example.beekeeper.domain.usecase.beehive_analytics

import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import javax.inject.Inject

class GetAnalyticsListByIdUseCase  @Inject constructor(
    private val beehiveAnalyticsRepository: BeehiveAnalyticsRepository
) {

    operator fun invoke(ids:List<Int>) =
        beehiveAnalyticsRepository.getAnalyticsListByIdsFromLocal(ids)

}