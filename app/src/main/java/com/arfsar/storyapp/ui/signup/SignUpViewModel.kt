package com.arfsar.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arfsar.storyapp.data.ResultState
import com.arfsar.storyapp.data.repository.UserRepository
import com.arfsar.storyapp.data.response.RegisterResponse

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<RegisterResponse>> = repository.registerUser(name, email, password)
}