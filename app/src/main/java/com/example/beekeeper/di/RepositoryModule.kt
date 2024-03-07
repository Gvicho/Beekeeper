package com.example.beekeeper.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.data.repository.AuthRepositoryImpl
import com.example.beekeeper.data.repository.DataStoreRepositoryImpl
import com.example.beekeeper.data.repository.FarmsRepositoryImpl
import com.example.beekeeper.data.repository.ReportRepositoryImpl
import com.example.beekeeper.data.repository.StorageRepositoryImpl
import com.example.beekeeper.data.source.remote.internet.service.FarmsService
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import com.example.beekeeper.domain.repository.farms.FarmsRepository
import com.example.beekeeper.domain.repository.save_credentials.CredentialsRepository
import com.example.beekeeper.domain.repository.storage.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
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
    fun provideReportRepository(
        @ApplicationContext context: Context
    ): ReportRepository {
        return ReportRepositoryImpl(
            context = context
        )
    }


    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, handleResponse: HandleResponse): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth,handleResponse)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>):CredentialsRepository {
        return DataStoreRepositoryImpl(
            datastore = dataStore
        )
    }

    @Provides
    @Singleton
    fun provideStorageRepository(storage: FirebaseStorage, database: FirebaseDatabase): StorageRepository =
        StorageRepositoryImpl(storage, database)


}