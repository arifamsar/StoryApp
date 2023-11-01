package com.arfsar.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.arfsar.storyapp.data.repository.MapsRepository

class MapsViewModel(private val mapsRepository: MapsRepository) : ViewModel() {
    fun getStoriesWithLocation() = mapsRepository.getStoriesWithLocation()
}