package com.example.valuematch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.valuematch.models.UserProfile

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    fun loadProfile() {
        // this is a temp mock data (replace with Firebase later)
        _userProfile.value = UserProfile(
            name = "Test User",
            age = 21,
            about = "This is a sample bio"
        )
    }
}

