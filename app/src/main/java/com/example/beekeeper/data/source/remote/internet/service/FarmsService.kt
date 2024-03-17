package com.example.beekeeper.data.source.remote.internet.service

import com.example.beekeeper.data.source.remote.internet.model.analytics.BeehiveAnalyticsDto
import com.example.beekeeper.data.source.remote.internet.model.farm.FarmDto
import com.example.beekeeper.data.source.remote.internet.model.farm.details.FarmDetailsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FarmsService {

    @GET("farms")
    suspend fun getFarms(): Response<List<FarmDto>>

    @GET("farm/{id}")
    suspend fun getFarmDetails(@Path("id") id: Int): Response<FarmDetailsDto>

    @POST("analytics/upload")
    suspend fun uploadAnalytics(@Body analyticsList: List<BeehiveAnalyticsDto>): Response<Unit>

}