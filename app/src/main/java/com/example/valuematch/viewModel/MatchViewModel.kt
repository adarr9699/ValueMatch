package com.example.valuematch.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valuematch.models.UserProfile
import com.example.valuematch.respository.MatchRepository
import com.example.valuematch.respository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MatchViewModel : ViewModel() {

    private val profileRepo = ProfileRepository()
    private val matchRepo = MatchRepository()

    private val _matches = MutableStateFlow(emptyList<UserProfile>())
    val matches: StateFlow<List<UserProfile>> = _matches

    fun loadMatches(currentUser: UserProfile) {
        viewModelScope.launch {
            val all = profileRepo.getAllProfiles()
            val results = matchRepo.getMatches(currentUser, all)
            _matches.value = results.map { UserProfile(uid = it.userId) }
        }
    }
}