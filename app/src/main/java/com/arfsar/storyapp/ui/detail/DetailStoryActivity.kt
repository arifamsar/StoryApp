package com.arfsar.storyapp.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.response.Story
import com.arfsar.storyapp.databinding.ActivityDetailStoryBinding
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.utils.Extra.EXTRA_DETAIL
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(EXTRA_DETAIL).toString()
        viewModel.detailStory(storyId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        getViewModel(result.data.story)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun getViewModel(story: Story) {
        with(binding) {
            Glide.with(this@DetailStoryActivity)
                .load(story.photoUrl)
                .skipMemoryCache(true)
                .into(ivDetail)
            tvTitle.text = story.name
            tvDescription.text = story.description
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}