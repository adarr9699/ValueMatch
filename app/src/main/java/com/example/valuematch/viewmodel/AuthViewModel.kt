package com.example.valuematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valuematch.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repo.login(email, password)
                .onSuccess { onResult(true, null) }
                .onFailure { onResult(false, it.message) }
        }
    }

    fun signup(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repo.signup(email, password)
                .onSuccess { onResult(true, null) }
                .onFailure { onResult(false, it.message) }
        }
    }
}
