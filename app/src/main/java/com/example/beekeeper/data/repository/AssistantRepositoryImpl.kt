package com.example.beekeeper.data.repository

import android.graphics.Bitmap
import android.util.Log.d
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.farmer_assistant.AssistantRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AssistantRepositoryImpl @Inject constructor(private val generativeModel: GenerativeModel) :
    AssistantRepository {
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


            emit(Resource.Failed("Failed"))
        }


    }

    override fun getSuggestions(): Flow<Resource<String>> = flow{
        try {
            emit(Resource.Loading())
            val prompt =  "I own beehive farm in kakheti lagodekhi. analize tomorrows weather and give me suggestions and advices for daily work."
            val response = generativeModel.generateContent(prompt)
            emit(Resource.Success(response.text.toString()))

        }catch (e: Exception){
            emit(Resource.Failed("Failed"))
        }
    }
}