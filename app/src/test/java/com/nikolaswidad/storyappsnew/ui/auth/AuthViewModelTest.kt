package com.nikolaswidad.storyappsnew.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nikolaswidad.storyappsnew.data.Resource
import com.nikolaswidad.storyappsnew.data.StoryRepository
import com.nikolaswidad.storyappsnew.data.network.response.LoginResponse
import com.nikolaswidad.storyappsnew.data.network.response.RegisterResponse
import com.nikolaswidad.storyappsnew.ui.DataDummy
import com.nikolaswidad.storyappsnew.ui.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var authViewModel: AuthViewModel
    private val dummyLogin = DataDummy.dummyLoginResponse()
    private val dummyRegister = DataDummy.dummyRegisterResponse()

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(storyRepository)
    }

    @Test
    fun login() {
        val expectedUser = MutableLiveData<Resource<LoginResponse>>()
        expectedUser.value = Resource.Success(dummyLogin)
        Mockito.`when`(storyRepository.login(EMAIL, PASSWORD)).thenReturn(expectedUser)

        val actualUser = authViewModel.login(EMAIL, PASSWORD).getOrAwaitValue()

        Mockito.verify(storyRepository).login(EMAIL, PASSWORD)
        assertNotNull(actualUser)
        assertTrue(actualUser is Resource.Success)
    }

    @Test
    fun register() {
        val expectedUser = MutableLiveData<Resource<RegisterResponse>>()
        expectedUser.value = Resource.Success(dummyRegister)
        Mockito.`when`(storyRepository.register(NAME, EMAIL, PASSWORD)).thenReturn(expectedUser)

        val actualUser = authViewModel.register(NAME, EMAIL, PASSWORD).getOrAwaitValue()

        Mockito.verify(storyRepository).register(NAME, EMAIL, PASSWORD)
        assertNotNull(actualUser)
        assertTrue(actualUser is Resource.Success)
    }

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}