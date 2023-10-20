package com.arfsar.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfsar.storyapp.R
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.response.ListStoryItem
import com.arfsar.storyapp.databinding.ActivityMainBinding
import com.arfsar.storyapp.ui.AddStoryActivity
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.adapter.StoryAdapter
import com.arfsar.storyapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginCheck()
        logout()
        getStories()

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun loginCheck() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun logout() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logOut -> {
                    viewModel.logout()
                    true
                }

                else -> false
            }
        }
    }

    private fun getStories() {
        viewModel.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {

                    }
                    is ResultState.Success -> {
                        setupRecyclerView()
                        setupViewModel(result.data.listStory)
                    }

                    is ResultState.Error -> {

                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val mLayoutManager =  LinearLayoutManager(this)
        binding.rvListStory.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {

            }

        })
    }

    private fun setupViewModel(storyItem: List<ListStoryItem>) {
        if (storyItem.isNotEmpty()) {
            binding.rvListStory.visibility = View.VISIBLE
            mAdapter.submitList(storyItem)
        } else {
            binding.rvListStory.visibility = View.INVISIBLE
        }
    }

}