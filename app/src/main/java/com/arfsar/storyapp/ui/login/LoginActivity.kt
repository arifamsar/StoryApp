package com.arfsar.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.pref.UserModel
import com.arfsar.storyapp.databinding.ActivityLoginBinding
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.main.MainActivity
import com.arfsar.storyapp.ui.utils.Extra.EXTRA_TOKEN

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
        playAnimation()
    }

    private fun setupAction() {
        binding.bvLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                email.isEmpty() -> error("Email can't be empty")
                password.isEmpty() -> error("Password can't be empty")
                else -> login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        viewModel.saveSessionToken(UserModel(email, result.data.loginResult.token))
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Congrats!")
                            setMessage(result.data.message)
                            setPositiveButton("Next") { _, _, ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra(EXTRA_TOKEN, result.data.loginResult.token)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val textPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val textEditEmail = ObjectAnimator.ofFloat(binding.emailInput, View.ALPHA, 1f).setDuration(500)
        val textEditPassword = ObjectAnimator.ofFloat(binding.passwordInput, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.bvLogin, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleLogin, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(title, textEmail, textEditEmail, textPassword, textEditPassword, button)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}