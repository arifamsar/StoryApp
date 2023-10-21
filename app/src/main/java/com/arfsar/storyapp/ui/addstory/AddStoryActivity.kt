package com.arfsar.storyapp.ui.addstory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.databinding.ActivityAddStoryBinding
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.main.MainActivity
import com.arfsar.storyapp.ui.utils.getImageUri
import com.arfsar.storyapp.ui.utils.reduceFileImage
import com.arfsar.storyapp.ui.utils.uriToFile

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { selectImage() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadStory() }

    }

    private fun selectImage() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val fileImage = uriToFile(uri, this).reduceFileImage()
            val description = binding.editTextDescription.text.toString()

            showLoading(true)

            viewModel.uploadStory(fileImage, description).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showLoading(false)
                            AlertDialog.Builder(this).apply {
                                setTitle("Success")
                                setMessage(result.data.message)
                                setPositiveButton("OK") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is ResultState.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

}