package com.nikolaswidad.storyappsnew.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.nikolaswidad.storyappsnew.R
import com.nikolaswidad.storyappsnew.data.network.response.ListStoryItem
import com.nikolaswidad.storyappsnew.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setDetail()
    }

    private fun setDetail() {
        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_DATA) as ListStoryItem

        binding.ivDetailPhoto.load(story.photoUrl) {
            crossfade(true)
            crossfade(500)
            placeholder(android.R.color.darker_gray)
            error(R.drawable.il_placeholder)
        }
        binding.tvTitleName.text = story.name
        binding.tvDetailDescription.text = story.description
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}