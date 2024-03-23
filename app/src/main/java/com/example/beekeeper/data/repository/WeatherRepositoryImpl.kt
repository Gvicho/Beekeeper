package com.example.beekeeper.data.repository

import com.example.beekeeper.data.source.remote.weather.mappers.toDomain
import com.example.beekeeper.data.source.remote.weather.service.WeatherService
import com.example.beekeeper.domain.common.Result
import com.example.beekeeper.domain.error_handling.DataError
import com.example.beekeeper.domain.error_handling.getNetworkErrorFromCode
import com.example.beekeeper.domain.model.weather.WeatherInfo
import com.example.beekeeper.domain.repository.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
) :WeatherRepository {
    override fun getWeather(lat: Double, lon: Double): Flow<Result<WeatherInfo,DataError.NetworkError>> {
        return flow {
            try {
                emit(Result.Loading())
                val response = weatherService.getWeather(lat = lat, lon = lon)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    emit(Result.Success(responseData = body.toDomain()))
                } else {
                    emit(Result.Failed(error = getNetworkErrorFromCode(response.code())))
                }
            } catch (e: HttpException) {
                emit(Result.Failed(error  = getNetworkErrorFromCode(e.code())))
            } catch (e : IOException){
                emit(Result.Failed(error  = DataError.NetworkError.NO_INTERNET))
            }
        }
    }
}

