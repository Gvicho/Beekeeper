package com.example.beekeeper.di


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.data.repository.AuthRepositoryImpl
import com.example.beekeeper.data.repository.DataStoreRepositoryImpl
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.example.beekeeper.domain.repository.save_credentials.CredentialsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    //add repository, here is example in comment just change names

//    @Singleton
//    @Provides
//    fun provideVehiclesRepository(
//        vehiclesApiService: VehiclesApiService,
//        handleResponse: HandleResponse
//    ): VehiclesRepository {
//        return VehiclesRepositoryImpl(
//            vehiclesApiService = vehiclesApiService,
//            handleResponse = handleResponse
//        )
//    }


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


}