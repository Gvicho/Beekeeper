package com.example.beekeeper.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.data.repository.AssistantRepositoryImpl
import com.example.beekeeper.data.repository.AuthRepositoryImpl
import com.example.beekeeper.data.repository.BeehiveAnalyticsRepositoryImpl
import com.example.beekeeper.data.repository.BeehiveAnalyticsUploadRepositoryImpl
import com.example.beekeeper.data.repository.DataStoreRepositoryImpl
import com.example.beekeeper.data.repository.FarmsRepositoryImpl
import com.example.beekeeper.data.repository.GetWeatherRepositoryImpl
import com.example.beekeeper.data.repository.ReportRepositoryImpl
import com.example.beekeeper.data.source.local.dao.BeehiveAnalyticsDao
import com.example.beekeeper.data.source.remote.internet.service.FarmsService
import com.example.beekeeper.data.source.remote.weather.service.WeatherService
import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsRepository
import com.example.beekeeper.domain.repository.analytics.BeehiveAnalyticsUploadRepository
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import com.example.beekeeper.domain.repository.farmer_assistant.AssistantRepository
import com.example.beekeeper.domain.repository.weather.GetWeatherRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideFarmsRepository(
        farmsService: FarmsService,
        handleResponse: HandleResponse
    ): FarmsRepository {
        return FarmsRepositoryImpl(
            farmsService = farmsService,
            handleResponse = handleResponse
        )
    }

    @Singleton
    @Provides
    fun provideAnalyticsUpload(
        farmsService: FarmsService,
        handleResponse: HandleResponse
    ): BeehiveAnalyticsUploadRepository {
        return BeehiveAnalyticsUploadRepositoryImpl(
            farmsService = farmsService,
            handleResponse = handleResponse
        )
    }

    @Singleton
    @Provides
    fun provideReportRepository(
        database: FirebaseDatabase,
        @ApplicationContext context: Context
    ): ReportRepository {
        return ReportRepositoryImpl(
            context = context,
            database = database
        )
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        handleResponse: HandleResponse
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, handleResponse)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(
            datastore = dataStore
        )
    }


    @Provides
    @Singleton
    fun provideBeehiveAnalyticsRepository(analyticsDao: BeehiveAnalyticsDao): BeehiveAnalyticsRepository {
        return BeehiveAnalyticsRepositoryImpl(analyticsDao)


    }

    @Provides
    @Singleton
    fun provideAssistantRepository(
        generativeModel: GenerativeModel,
        @ApplicationContext context: Context
    ): AssistantRepository {
        return AssistantRepositoryImpl(generativeModel, context)


    }

    @Provides
    @Singleton
    fun provideGetWeatherRepository(
       weatherService: WeatherService,
       handleResponse: HandleResponse
    ): GetWeatherRepository {
        return GetWeatherRepositoryImpl(weatherService,handleResponse)


    }

}