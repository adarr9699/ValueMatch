package com.example.valuematch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.valuematch.models.MatchResult

class MatchViewModel : ViewModel() {

    private val _bestMatches = MutableLiveData<List<MatchResult>>()
    val bestMatches: LiveData<List<MatchResult>> = _bestMatches

    fun loadMatches() {

        _bestMatches.value = listOf(
            MatchResult("user123", 85, listOf("Honesty", "Family")),
            MatchResult("user456", 72, listOf("Growth"))
        )
    }
}
