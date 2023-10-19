package com.arfsar.storyapp.data.di

import android.content.Context
import com.arfsar.storyapp.data.UserRepository
import com.arfsar.storyapp.data.pref.UserPreference
import com.arfsar.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}