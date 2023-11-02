package com.arfsar.storyapp.data.di

import android.content.Context
import com.arfsar.storyapp.data.repository.UserRepository
import com.arfsar.storyapp.data.api.ApiConfig
import com.arfsar.storyapp.data.database.StoryDatabase
import com.arfsar.storyapp.data.pref.UserPreference
import com.arfsar.storyapp.data.pref.dataStore
import com.arfsar.storyapp.data.repository.MapsRepository
import com.arfsar.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context) : UserRepository {
        val userPref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(userPref, apiService)
    }

    fun provideStoryRepository(context: Context) : StoryRepository {
        val userPref = UserPreference.getInstance(context.dataStore)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(userPref, storyDatabase)
    }

    fun provideMapRepository(context: Context) : MapsRepository {
        val userPref = UserPreference.getInstance(context.dataStore)
        return MapsRepository.getInstance(userPref)
    }

}