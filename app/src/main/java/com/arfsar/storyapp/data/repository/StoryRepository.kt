package com.arfsar.storyapp.data.repository

import androidx.lifecycle.liveData
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.api.ApiConfig
import com.arfsar.storyapp.data.api.ApiService
import com.arfsar.storyapp.data.pref.UserPreference
import com.arfsar.storyapp.data.response.DetailResponse
import com.arfsar.storyapp.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class StoryRepository private constructor(
    private val apiService: ApiService,
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

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
