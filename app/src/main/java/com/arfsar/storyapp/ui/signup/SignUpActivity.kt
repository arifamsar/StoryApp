package com.arfsar.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.databinding.ActivitySignUpBinding
import com.arfsar.storyapp.ui.ViewModelFactory
import com.arfsar.storyapp.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.bvRegister.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditText.error = "Name can't be empty"
                    binding.nameEditText.requestFocus()
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    binding.emailEditText.error = "Email can't be empty"
                    binding.emailEditText.requestFocus()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = "Password can't be empty"
                    binding.passwordEditText.requestFocus()
                    return@setOnClickListener
                }
                else -> register(name, email, password)
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModel.register(name, email, password).observe(this) { result ->
            if (result != null) {
                when(result){
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Congrats!")
                            setMessage(result.data.toString())
                            setPositiveButton("Next") { _, _ ->
                                val intent = Intent(context, LoginActivity::class.java)
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivSignup, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val textName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val textPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val textEditName = ObjectAnimator.ofFloat(binding.nameInput, View.ALPHA, 1f).setDuration(500)
        val textEditEmail = ObjectAnimator.ofFloat(binding.emailInput, View.ALPHA, 1f).setDuration(500)
        val textEditPassword = ObjectAnimator.ofFloat(binding.passwordInput, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.bvRegister, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleSignUp, View.ALPHA, 1f).setDuration(500)

        val name = AnimatorSet().apply {
            playTogether(textName, textEditName)
        }
        val email = AnimatorSet().apply {
            playTogether(textEmail, textEditEmail)
        }
        val password = AnimatorSet().apply {
            playTogether(textPassword, textEditPassword)
        }
        AnimatorSet().apply {
            playSequentially(title, name, email, password, button)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}