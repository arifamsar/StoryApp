package com.arfsar.storyapp.data.repository

import androidx.lifecycle.liveData
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.api.ApiConfig
import com.arfsar.storyapp.data.pref.UserPreference
import com.arfsar.storyapp.data.response.DetailResponse
import com.arfsar.storyapp.data.response.StoryResponse
import com.arfsar.storyapp.data.response.StoryUploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val userPreference: UserPreference
) {
    fun getAllStories() = liveData {
        emit(ResultState.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val client = apiService.getAllStories()
            emit(ResultState.Success(client))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let {
                ResultState.Error(it)
            }?.let { emit(it) }
        }
    }

    fun detailStories(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val client = apiService.detailStory(id)
            emit(ResultState.Success(client))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, DetailResponse::class.java)
            errorBody?.message?.let {
                ResultState.Error(it)
            }?.let { emit(it) }
        }
    }

    fun uploadStory(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.uploadStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryUploadResponse::class.java)
            errorBody?.message?.let {
                ResultState.Error(it)
            }?.let { emit(it) }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(userPreference: UserPreference): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository( userPreference)
            }.also { instance = it }
    }
}
