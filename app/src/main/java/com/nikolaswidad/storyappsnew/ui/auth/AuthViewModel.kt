package com.nikolaswidad.storyappsnew.ui.auth

import androidx.lifecycle.ViewModel
import com.nikolaswidad.storyappsnew.data.StoryRepository

class AuthViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    fun login(email: String, pass: String) = repository.login(email, pass)

    fun register(name: String, email: String, pass: String) = repository.register(name, email, pass)
}