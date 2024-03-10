package com.example.beekeeper.data.repository

import com.example.beekeeper.data.source.local.dao.BeehiveAnalyticsDao
import com.example.beekeeper.data.source.local.database.AppDatabase
import com.example.beekeeper.data.source.local.mapper.toData
import com.example.beekeeper.data.source.local.mapper.toDomain
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.BeehiveAnalytics
import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BeehiveAnalyticsRepositoryImpl @Inject constructor(private val analyticsDao: BeehiveAnalyticsDao) :
    BeehiveAnalyticsRepository {
    override suspend fun getAnalyticsFromLocal(): Flow<Resource<List<BeehiveAnalytics>>> =
        withContext(Dispatchers.IO) {
            flow {
                try {
                    emit(Resource.Loading())
                    val res = analyticsDao.getAllAnalytics()

                    emit(Resource.Success(res.map { it.toDomain() }))
                } catch (e: Exception) {
                    emit(Resource.Failed("Error"))
                }


            }
        }

    override suspend fun getAnalyticsByIdFromLocal(id: Int): Flow<Resource<BeehiveAnalytics>> =
        withContext(Dispatchers.IO) {
            flow {
                try {


                    emit(Resource.Loading())
                    val res = analyticsDao.getAnalyticsById(id)
                    if (res != null) {
                        emit(Resource.Success(res.toDomain()))
                    } else {
                        emit(Resource.Failed("Not Found"))
                    }
                } catch (e: Exception) {
                    emit(Resource.Failed("Error"))
                }
            }

        }

    override suspend fun deleteAllFromLocal(): Flow<Resource<Boolean>> =
        withContext(Dispatchers.IO)
        {
            flow {
                try {


                    emit(Resource.Loading())
                    analyticsDao.deleteAll()
                    emit(Resource.Success(true))
                } catch (e: Exception) {
                    emit(Resource.Failed("Error"))
                }
            }
        }


    override suspend fun insertAnalyticsInLocal(analytics: BeehiveAnalytics): Flow<Resource<Long>> =
        withContext(Dispatchers.IO)
        {
            flow {
                try {


                    emit(Resource.Loading())
                    val res = analyticsDao.insertAnalytics(analytics.toData())
                    emit(Resource.Success(res))
                } catch (e: Exception) {
                    emit(Resource.Failed("Error"))
                }
            }
        }

}