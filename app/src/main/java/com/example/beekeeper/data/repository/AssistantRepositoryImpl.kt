package com.example.beekeeper.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log.d
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.farmer_assistant.AssistantRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssistantRepositoryImpl @Inject constructor(private val generativeModel: GenerativeModel, private val context: Context) :
    AssistantRepository {
    override fun getDescription(imagesUris: List<Uri>): Flow<Resource<String>> = flow {
        try {

            emit(Resource.Loading())

            val imagesBitmaps = imagesUris.mapNotNull { uri ->
                d("ImagesRepo",imagesUris.toString())
                uri.toBitmap()?.let { bitmap ->
                    d("BITMAPNOTMnUL",imagesUris.toString())
                    correctOrientation(uri, bitmap)
                }
            }
            val inputContent = content {
                imagesBitmaps.forEach { imageBitmap ->
                    image(imageBitmap)

                }
                text("Analyze these pictures of damaged beehives. give me possible reasons of damage, time since the damage, weather conditions, potential and if bees were harm. also risks associated and recommendations")
            }

            val response = generativeModel.generateContent(inputContent)
            emit(Resource.Success(response.text.toString()))
        } catch (e: Exception) {
            d("errorinRepoAnalys", e.toString())
            emit(Resource.Failed("Failed: ${e.localizedMessage}"))
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


    private suspend fun Uri.toBitmap(): Bitmap? = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(this@toBitmap)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)

            }
        } catch (e: Exception) {
            d("errorInFun", e.toString())
            null
        }
    }

    private suspend fun correctOrientation(uri: Uri, bitmap: Bitmap): Bitmap = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val exif = ExifInterface(inputStream)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )

                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                }

                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } ?: bitmap
        } catch (e: Exception) {
            bitmap // Return the original bitmap if there was an error
        }
    }
}