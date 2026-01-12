package com.example.valuematch.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.valuematch.models.MatchResult
import com.example.valuematch.repository.AuthRepository
import com.example.valuematch.repository.ProfileRepository
import com.example.valuematch.utils.MatchingUtils

class MatchViewModel : ViewModel() {

    private val authRepo = AuthRepository()
    private val profileRepo = ProfileRepository()

    private val _matches = MutableLiveData<List<MatchResult>>()
    val matches: LiveData<List<MatchResult>> = _matches

    private var currentIndex = 0

    val currentMatchUserId: String?
        get() = _matches.value?.getOrNull(currentIndex)?.userId

    fun loadMatches() {
        val uid = authRepo.currentUserId() ?: return

        profileRepo.getAllUsers { users ->
            val currentUser = users.find { it.uid == uid } ?: return@getAllUsers

            val results = users
                .filter { it.uid != uid }
                .map { MatchingUtils.calculateMatch(currentUser, it) }

            _matches.postValue(results)
        }
    }

    fun nextMatch() {
        currentIndex++
        _matches.value = _matches.value
    }
}

