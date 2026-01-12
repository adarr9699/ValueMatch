package com.example.valuematch.viewModel

import androidx.lifecycle.ViewModel
import com.example.valuematch.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    fun signup(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        repo.signup(email, password, callback)
    }

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        repo.login(email, password, callback)
    }
}
