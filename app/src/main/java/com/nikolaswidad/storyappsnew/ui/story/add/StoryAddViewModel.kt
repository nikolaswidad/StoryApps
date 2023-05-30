package com.nikolaswidad.storyappsnew.ui.story.add

import androidx.lifecycle.ViewModel
import com.nikolaswidad.storyappsnew.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAddViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    fun postImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double,
        token: String,
        multiPort: String,
    ) = repository.postImage(file, description, lat, lon, token, multiPort)
}