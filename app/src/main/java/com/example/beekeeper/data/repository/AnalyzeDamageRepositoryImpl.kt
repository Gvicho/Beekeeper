package com.example.beekeeper.data.repository

import android.graphics.Bitmap
import android.util.Log.d
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.evaluator.AnalyzeDamageRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnalyzeDamageRepositoryImpl @Inject constructor(private val generativeModel: GenerativeModel) :
    AnalyzeDamageRepository {
    override fun getDescription(images: List<Bitmap>): Flow<Resource<String>> = flow{

        try {
            emit(Resource.Loading())

            val inputContent = content {
                images.forEach{imageBitmap ->
                    image(imageBitmap)

                }
                text("Analyze these pictures of damaged beehives. give me possible reasons of damage, time since the damage, weather conditions, potential and if bees were harm. also risks associated and recommendations")
            }
            val response = generativeModel.generateContent(inputContent)
            emit(Resource.Success(response.text.toString()))

        }catch (e: Exception){

            d("failedRepo", e.toString())
            d("imagesRepoFail", images.toString())
            emit(Resource.Failed("Failed"))
        }


    }
}