package com.nikolaswidad.storyappsnew.ui.story.maps

import androidx.lifecycle.ViewModel
import com.nikolaswidad.storyappsnew.data.StoryRepository

class StoryMapsViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    fun getStoriesWithLocation(location: Int, token: String) =
        repository.getStoriesWithLocation(location, token)
}