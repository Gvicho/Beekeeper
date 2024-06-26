package com.example.beekeeper.di


import android.content.Context
import com.example.beekeeper.BuildConfig
import com.example.beekeeper.data.source.remote.bluetooth.conroller.BluetoothControllerImpl
import com.example.beekeeper.data.source.remote.internet.service.FarmsService
import com.example.beekeeper.data.source.remote.weather.service.WeatherService
import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import com.example.beekeeper.domain.repository.user.UserRepository
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule { //stuff that are here should be singleton


    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Set the log level to NONE when it's not a debug build
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        // Add the logging interceptor in debug mode
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @Named("FarmsService")
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.POSTMAN_BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("WeatherService")
    fun provideRetrofitClientForWeather(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideBluetoothController(
        @ApplicationContext context: Context
    ): BluetoothController {
        return BluetoothControllerImpl(
            context = context
        )
    }

    @Singleton
    @Provides
    fun provideFarmsService(@Named("FarmsService") retrofit: Retrofit): FarmsService {
        return retrofit.create(FarmsService::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherService(@Named("WeatherService") retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }


    @Singleton
    @Provides
    fun provideUseCase(repository: UserRepository): WriteUserDataUseCase {
        return WriteUserDataUseCase(userRepository = repository)
    }


}