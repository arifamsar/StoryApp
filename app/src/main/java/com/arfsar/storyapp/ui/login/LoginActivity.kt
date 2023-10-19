package com.arfsar.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.arfsar.storyapp.data.pref.UserModel
import com.arfsar.storyapp.databinding.ActivityLoginBinding
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.bvLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            viewModel.saveSession(UserModel(email, "sample_token" ))
            AlertDialog.Builder(this).apply {
                setTitle("Congrats!")
                setMessage("Login Succesful")
                setPositiveButton("Next") { _, _, ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }
}