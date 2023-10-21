package com.arfsar.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.arfsar.storyapp.data.repository.StoryRepository

class DetailStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun detailStory(id: String) = repository.detailStories(id)
}