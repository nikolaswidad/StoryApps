package com.nikolaswidad.storyappsnew.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikolaswidad.storyappsnew.R
import com.nikolaswidad.storyappsnew.ViewModelFactory
import com.nikolaswidad.storyappsnew.data.network.response.ListStoryItem
import com.nikolaswidad.storyappsnew.databinding.ActivityMainBinding
import com.nikolaswidad.storyappsnew.preferences.UserPreferences
import com.nikolaswidad.storyappsnew.ui.auth.LoginActivity
import com.nikolaswidad.storyappsnew.ui.story.add.StoryAddActivity
import com.nikolaswidad.storyappsnew.ui.story.detail.StoryDetailActivity
import com.nikolaswidad.storyappsnew.ui.story.maps.StoryMapsActivity
import com.nikolaswidad.storyappsnew.ui.utils.hide

class MainActivity : AppCompatActivity(), StoryAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var preferences: UserPreferences
    private lateinit var factory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        preferences = UserPreferences(this)

        binding.fabStoryAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, StoryAddActivity::class.java))
        }
        binding.actLogout.setOnClickListener {
            dialogLogout()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        binding.actLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.actMap.setOnClickListener {
            startActivity(Intent(this@MainActivity, StoryMapsActivity::class.java))
        }
        setStory()
    }

    private fun setStory() {
        val token = preferences.getToken() ?: ""
        getStory(token)
    }

    private fun getStory(token: String) {
        storyAdapter = StoryAdapter(this)
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        val userToken = "Bearer $token"
        viewModel.getStory(userToken).observe(this) {
            storyAdapter.submitData(lifecycle, it)
            binding.progressCircular.hide()
        }
    }

    private fun dialogLogout() {
        AlertDialog.Builder(this).apply {
            setTitle("Logout")
            setMessage("Exit this app?")
            setCancelable(false)
            setPositiveButton("Ok") { _, _ ->
                preferences.setToken(null)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun onListener(story: ListStoryItem) {
        startActivity(
            Intent(this@MainActivity, StoryDetailActivity::class.java)
                .putExtra(StoryDetailActivity.EXTRA_DATA, story)
        )
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}