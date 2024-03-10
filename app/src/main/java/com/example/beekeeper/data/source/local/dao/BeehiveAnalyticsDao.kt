package com.example.beekeeper.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beekeeper.data.source.local.entity.BeehiveAnalyticsEntity

@Dao
interface BeehiveAnalyticsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnalytics(analytics: BeehiveAnalyticsEntity): Long

    @Query("SELECT * FROM BeehiveAnalyticsEntity")
    suspend fun getAllAnalytics(): List<BeehiveAnalyticsEntity>

    @Query("SELECT * FROM BeehiveAnalyticsEntity WHERE id = :id")
    suspend fun getAnalyticsById(id: Int): BeehiveAnalyticsEntity?

    @Query("DELETE FROM BeehiveAnalyticsEntity")
    suspend fun deleteAll()

}