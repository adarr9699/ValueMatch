package com.example.valuematch.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valuematch.models.UserProfile
import com.example.valuematch.respository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repo = ProfileRepository()

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repo.saveProfile(profile)
        }
    }
}