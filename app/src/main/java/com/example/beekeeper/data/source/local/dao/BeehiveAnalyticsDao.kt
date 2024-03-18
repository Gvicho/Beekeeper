package com.example.beekeeper.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beekeeper.data.source.local.model.BeehiveAnalyticsEntity
import com.example.beekeeper.data.source.local.model.SavedAnalyticsPartialData

@Dao
interface BeehiveAnalyticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalytics(analytics: BeehiveAnalyticsEntity): Long // if constraints were violated -1 will be returned. conflict is solved by replacing

    @Query("SELECT * FROM BeehiveAnalyticsEntity")
    suspend fun getAllAnalytics(): List<BeehiveAnalyticsEntity>

    @Query("SELECT * FROM BeehiveAnalyticsEntity WHERE id = :id")
    suspend fun getAnalyticsById(id: Int): BeehiveAnalyticsEntity?

    @Query("DELETE FROM BeehiveAnalyticsEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM BeehiveAnalyticsEntity WHERE id = :id")
    suspend fun deleteAnalyticsById(id: Int)

    @Query("SELECT id, saveDateTime FROM BeehiveAnalyticsEntity")
    suspend fun getAllAnalyticsPartial(): List<SavedAnalyticsPartialData>

    @Query("SELECT * FROM BeehiveAnalyticsEntity WHERE id IN (:ids)")
    suspend fun getAnalyticsListByIds(ids: List<Int>): List<BeehiveAnalyticsEntity>

    @Query("SELECT id, saveDateTime FROM BeehiveAnalyticsEntity ORDER BY saveDateTime ASC")
    suspend fun getAllAnalyticsPartialSortedOldestToNewest(): List<SavedAnalyticsPartialData>

    @Query("SELECT id, saveDateTime FROM BeehiveAnalyticsEntity ORDER BY saveDateTime DESC")
    suspend fun getAllAnalyticsPartialSortedNewestToOldest(): List<SavedAnalyticsPartialData>

}