package com.example.beekeeper.domain.usecase.beehive_analytics

import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import com.example.beekeeper.domain.utils.Order
import javax.inject.Inject

class GetAllAnalyticsUseCase  @Inject constructor(private val beehiveAnalyticsRepository: BeehiveAnalyticsRepository) {

    operator fun invoke(order:Order) =
        beehiveAnalyticsRepository.getPartialAnalyticsFromLocal(order)

}