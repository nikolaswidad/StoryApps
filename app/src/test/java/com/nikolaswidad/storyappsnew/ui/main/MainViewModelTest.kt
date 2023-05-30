package com.nikolaswidad.storyappsnew.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.nikolaswidad.storyappsnew.data.StoryRepository
import com.nikolaswidad.storyappsnew.data.network.response.ListStoryItem
import com.nikolaswidad.storyappsnew.ui.DataDummy
import com.nikolaswidad.storyappsnew.ui.MainDispatcherRule
import com.nikolaswidad.storyappsnew.ui.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var story: MutableLiveData<PagingData<ListStoryItem>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        story = MutableLiveData()
    }

    @Test
    fun getStory() = runTest {
        val dummyStory = DataDummy.dummyStoryResponse()
        val data: PagingData<ListStoryItem> = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data
        `when`(storyRepository.getStories(TOKEN)).thenReturn(story)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<ListStoryItem> = mainViewModel.getStory(TOKEN).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        advanceUntilIdle()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)

        val firstItem = differ.snapshot().items.firstOrNull()
        Assert.assertNotNull(firstItem)
        Assert.assertEquals(dummyStory[0], firstItem)

    }

    @Test
    fun getStory_emptyData_returnsEmpty() = runTest {
        val emptyStory = emptyList<ListStoryItem>()
        val emptyData: PagingData<ListStoryItem> = PagedTestDataSources.snapshot(emptyStory)
        story.value = emptyData
        `when`(storyRepository.getStories(TOKEN)).thenReturn(story)

        val mainViewModel = MainViewModel(storyRepository)
        val actualEmptyStory: PagingData<ListStoryItem> = mainViewModel.getStory(TOKEN).getOrAwaitValue()

        val emptyDiffer = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        emptyDiffer.submitData(actualEmptyStory)

        advanceUntilIdle()

        Assert.assertNotNull(emptyDiffer.snapshot())
        Assert.assertEquals(0, emptyDiffer.snapshot().size)
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN"
    }
}

class PagedTestDataSources private constructor() :
    PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
