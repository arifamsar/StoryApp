package com.arfsar.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.api.ApiService
import com.arfsar.storyapp.data.pref.UserModel
import com.arfsar.storyapp.data.pref.UserPreference
import com.arfsar.storyapp.data.response.ErrorResponse
import com.arfsar.storyapp.data.response.LoginResponse
import com.arfsar.storyapp.data.response.RegisterResponse
import com.arfsar.storyapp.helper.wrapEspressoIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val client = apiService.registerUser(name, email, password)
            emit(ResultState.Success(client))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            errorBody.message?.let {
                ResultState.Error(it)
            }?.let { emit(it) }
        }
    }

    fun loginUser(
        email: String,
        password: String
    ) = liveData {
        emit(ResultState.Loading)
        wrapEspressoIdlingResource {
            try {
                val client = apiService.loginUser(email, password)
                emit(ResultState.Success(client))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                errorBody.message.let {
                    ResultState.Error(it)
                }.let { emit(it) }
            }
        }

    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiService)
        }.also { instance = it }
    }
}