package com.arfsar.storyapp.data.repository

import androidx.lifecycle.liveData
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.api.ApiConfig
import com.arfsar.storyapp.data.pref.UserPreference
import com.arfsar.storyapp.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class MapsRepository private constructor(
    private val userPreference: UserPreference
) {
    fun getStoriesWithLocation() = liveData {
        emit(ResultState.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val client = apiService.getStoriesWithLocation()
            emit(ResultState.Success(client))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let {
                ResultState.Error(it)
            }?.let { emit(it) }
        }
    }

    companion object {
        @Volatile
        private var instance: MapsRepository? = null
        fun getInstance(userPreference: UserPreference): MapsRepository =
            instance ?: synchronized(this) {
                instance ?: MapsRepository( userPreference)
            }.also { instance = it }
    }
}
