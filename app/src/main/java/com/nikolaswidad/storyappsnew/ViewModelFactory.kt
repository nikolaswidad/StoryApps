package com.nikolaswidad.storyappsnew

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikolaswidad.storyappsnew.data.StoryRepository
import com.nikolaswidad.storyappsnew.di.Injection
import com.nikolaswidad.storyappsnew.ui.auth.AuthViewModel
import com.nikolaswidad.storyappsnew.ui.main.MainViewModel
import com.nikolaswidad.storyappsnew.ui.story.add.StoryAddViewModel
import com.nikolaswidad.storyappsnew.ui.story.maps.StoryMapsViewModel

class ViewModelFactory private constructor(
    private val repository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryAddViewModel::class.java) -> {
                StoryAddViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryMapsViewModel::class.java) -> {
                StoryMapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}