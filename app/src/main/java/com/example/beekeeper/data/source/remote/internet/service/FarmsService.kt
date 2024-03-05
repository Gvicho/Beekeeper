package com.example.beekeeper.data.source.remote.internet.service

import com.example.beekeeper.data.source.remote.internet.model.FarmDto
import retrofit2.Response
import retrofit2.http.GET

interface FarmsService {

    @GET("farms")
    suspend fun getFarms(): Response<List<FarmDto>>

}