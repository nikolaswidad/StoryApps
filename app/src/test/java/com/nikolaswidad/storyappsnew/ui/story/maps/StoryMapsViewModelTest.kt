package com.nikolaswidad.storyappsnew.ui.story.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nikolaswidad.storyappsnew.data.Resource
import com.nikolaswidad.storyappsnew.data.StoryRepository
import com.nikolaswidad.storyappsnew.data.network.response.StoryResponse
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
class StoryMapsViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyWithMapsViewModel: StoryMapsViewModel
    private val dummyStoryWithMaps = DataDummy.dummyStoryWithLocationResponse()

    @Before
    fun setUp() {
        storyWithMapsViewModel = StoryMapsViewModel(storyRepository)
    }

    @Test
    fun getStoryWithLocation() {
        val expectedStory = MutableLiveData<Resource<StoryResponse>>()
        expectedStory.value = Resource.Success(dummyStoryWithMaps)
        Mockito.`when`(storyRepository.getStoriesWithLocation(LOCATION, TOKEN))
            .thenReturn(expectedStory)

        val actualStory =
            storyWithMapsViewModel.getStoriesWithLocation(LOCATION, TOKEN).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(LOCATION, TOKEN)
        assertNotNull(actualStory)
        assertTrue(actualStory is Resource.Success)
    }

    companion object {
        private const val LOCATION = 1
        private const val TOKEN = "Bearer TOKEN"
    }
}