package com.nikolaswidad.storyappsnew.di

import android.content.Context
import com.nikolaswidad.storyappsnew.data.StoryRepository
import com.nikolaswidad.storyappsnew.data.local.StoryDatabase
import com.nikolaswidad.storyappsnew.data.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}