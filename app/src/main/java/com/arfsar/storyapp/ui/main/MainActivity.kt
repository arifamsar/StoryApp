package com.arfsar.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfsar.storyapp.R
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.response.ListStoryItem
import com.arfsar.storyapp.databinding.ActivityMainBinding
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.adapter.StoryAdapter
import com.arfsar.storyapp.ui.addstory.AddStoryActivity
import com.arfsar.storyapp.ui.detail.DetailStoryActivity
import com.arfsar.storyapp.ui.utils.Extra.EXTRA_DETAIL
import com.arfsar.storyapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        setupRecyclerView()
                        setupViewModel(result.data.listStory)
                    }

                    is ResultState.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        showLoading(false)
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
                getDetailStory(data)
            }

        })
    }

    private fun getDetailStory(storyItem: ListStoryItem) {
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(EXTRA_DETAIL, storyItem.id)
        startActivity(intent)
    }

    private fun setupViewModel(storyItem: List<ListStoryItem>) {
        if (storyItem.isNotEmpty()) {
            binding.rvListStory.visibility = View.VISIBLE
            mAdapter.submitList(storyItem)
        } else {
            binding.rvListStory.visibility = View.INVISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

}