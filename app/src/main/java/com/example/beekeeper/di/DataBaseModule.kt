package com.example.beekeeper.di

import android.content.Context
import androidx.room.Room
import com.example.beekeeper.data.source.local.dao.BeehiveAnalyticsDao
import com.example.beekeeper.data.source.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "BeeKeeperDb"
        )
            .build()
    }


    @Singleton
    @Provides
    fun provideAnalyticsDao(appDatabase: AppDatabase): BeehiveAnalyticsDao {
        return appDatabase.beehiveAnalyticsDao()
    }

}