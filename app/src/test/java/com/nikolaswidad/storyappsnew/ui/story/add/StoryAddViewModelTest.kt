package com.nikolaswidad.storyappsnew.ui.story.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nikolaswidad.storyappsnew.data.Resource
import com.nikolaswidad.storyappsnew.data.StoryRepository
import com.nikolaswidad.storyappsnew.data.network.response.FileUploadResponse
import com.nikolaswidad.storyappsnew.ui.DataDummy
import com.nikolaswidad.storyappsnew.ui.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class StoryAddViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addViewModel: StoryAddViewModel
    private val dummyAddStory = DataDummy.dummyAddStoryResponse()

    @Before
    fun setUp() {
        addViewModel = StoryAddViewModel(storyRepository)
    }

    @Test
    fun postImage() {
        val description = "This is Description".toRequestBody("text/plain".toMediaType())
        val file = Mockito.mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImageFile
        )

        val expectedStory = MutableLiveData<Resource<FileUploadResponse>>()
        expectedStory.value = Resource.Success(dummyAddStory)
        Mockito.`when`(
            storyRepository.postImage(
                imageMultipart, description, LAT, LON, TOKEN,
                ACCEPT
            )
        ).thenReturn(expectedStory)

        val actualStory = addViewModel.postImage(
            imageMultipart, description, LAT, LON, TOKEN,
            ACCEPT
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).postImage(
            imageMultipart, description, LAT, LON, TOKEN,
            ACCEPT
        )
        assertNotNull(actualStory)
        assertTrue(actualStory is Resource.Success)
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN"
        private const val LAT = 1.00
        private const val LON = 1.00
        private const val ACCEPT = "application/json"
    }
}