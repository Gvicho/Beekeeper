package com.example.beekeeper.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beekeeper.data.source.local.dao.BeehiveAnalyticsDao
import com.example.beekeeper.data.source.local.model.BeehiveAnalyticsEntity

@Database(entities = [BeehiveAnalyticsEntity::class],version = 1, exportSchema = false)
abstract  class  AppDatabase: RoomDatabase(){

    abstract  fun beehiveAnalyticsDao(): BeehiveAnalyticsDao

}