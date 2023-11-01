package com.arfsar.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arfsar.storyapp.data.pref.UserModel
import com.arfsar.storyapp.data.repository.StoryRepository
import com.arfsar.storyapp.data.repository.UserRepository
import com.arfsar.storyapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    storyRepository: StoryRepository
) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

//    fun getStories() = storyRepository.getAllStories()

    val stories: LiveData<PagingData<ListStoryItem>> = storyRepository.getStories().cachedIn(viewModelScope)
}