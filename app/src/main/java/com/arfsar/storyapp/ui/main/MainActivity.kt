package com.arfsar.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfsar.storyapp.R
import com.arfsar.storyapp.data.response.ListStoryItem
import com.arfsar.storyapp.databinding.ActivityMainBinding
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.adapter.LoadingStateAdapter
import com.arfsar.storyapp.ui.adapter.StoryAdapter
import com.arfsar.storyapp.ui.addstory.AddStoryActivity
import com.arfsar.storyapp.ui.detail.DetailStoryActivity
import com.arfsar.storyapp.ui.map.MapsActivity
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
        optionMenu()

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getDataStories()
    }

    private fun loginCheck() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.stories.observe(this) { data ->
                    if (data != null) {
                        getDataStories()
                        showLoading(false)
                    } else {
                        showLoading(true)
                        Toast.makeText(this, getString(R.string.story_empty), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun optionMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logOut -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.logout))
                        setMessage(getString(R.string.logout_reminder))
                        setPositiveButton(getString(R.string.logout)) { _, _ ->
                            viewModel.logout()
                        }
                        setNegativeButton(getString(R.string.cancel)) { _, _ ->
                            // Do Nothing
                        }
                        create()
                        show()
                    }
                    true
                }
                R.id.mapActivity -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
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

    private fun getDataStories() {
//
//        mAdapter.withLoadStateFooter(
//            footer = LoadingStateAdapter {
//                mAdapter.retry()
//            }
//        )
        binding.rvListStory.adapter = mAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                mAdapter.retry()
            }
        )

        viewModel.stories.observe(this) {
            mAdapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

}