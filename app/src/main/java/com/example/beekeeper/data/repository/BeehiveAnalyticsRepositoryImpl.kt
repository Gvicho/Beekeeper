package com.example.beekeeper.data.repository

import com.example.beekeeper.data.source.local.dao.BeehiveAnalyticsDao
import com.example.beekeeper.data.source.local.mapper.toDomain
import com.example.beekeeper.data.source.local.mapper.toEntity
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import com.example.beekeeper.domain.model.analytics.SavedAnalyticsPartial
import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import com.example.beekeeper.domain.utils.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BeehiveAnalyticsRepositoryImpl @Inject constructor(private val analyticsDao: BeehiveAnalyticsDao) :
    BeehiveAnalyticsRepository {

    override fun getPartialAnalyticsFromLocal(order:Order): Flow<Resource<List<SavedAnalyticsPartial>>> =
        flow {
            try {
                emit(Resource.Loading())

                val res = when(order){
                    Order.NONE -> analyticsDao.getAllAnalyticsPartial()
                    Order.ASCENDING -> analyticsDao.getAllAnalyticsPartialSortedOldestToNewest()
                    Order.DESCENDING -> analyticsDao.getAllAnalyticsPartialSortedNewestToOldest()
                }
                emit(Resource.Success(res.map { it.toDomain() }))
            } catch (e: Exception) {
                emit(Resource.Failed("Error"))
            }
        }.flowOn(Dispatchers.IO)

    override fun getAnalyticsByIdFromLocal(id: Int): Flow<Resource<BeehiveAnalytics>> =
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
        }.flowOn(Dispatchers.IO)

    override fun deleteAllFromLocal(): Flow<Resource<Unit>> =
        flow {
            try {
                emit(Resource.Loading())
                analyticsDao.deleteAll()
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Failed("Error"))
            }
        }.flowOn(Dispatchers.IO)

    override fun insertAnalyticsInLocal(analytics: BeehiveAnalytics): Flow<Resource<Long>> =
        flow {
            try {
                emit(Resource.Loading())
                val res = analyticsDao.insertAnalytics(analytics.toEntity())
                emit(Resource.Success(res))
            } catch (e: Exception) {
                emit(Resource.Failed("Error"))
            }
        }.flowOn(Dispatchers.IO)

    override fun deleteAnalyticsByIdFromLocal(id: Int): Flow<Resource<Unit>> =
        flow {
            try {
                emit(Resource.Loading())
                analyticsDao.deleteAnalyticsById(id)
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Failed("Error"))
            }
        }.flowOn(Dispatchers.IO)

    override fun getAnalyticsListByIdsFromLocal(ids: List<Int>): Flow<Resource<List<BeehiveAnalytics>>> =
        flow {
            try {
                emit(Resource.Loading())
                val res = analyticsDao.getAnalyticsListByIds(ids)
                emit(Resource.Success(res.map { it.toDomain() }))
            } catch (e: Exception) {
                emit(Resource.Failed("Error"))
            }
        }.flowOn(Dispatchers.IO)
}