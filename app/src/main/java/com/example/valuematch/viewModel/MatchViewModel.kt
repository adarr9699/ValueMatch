package com.example.valuematch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.valuematch.models.MatchResult

class MatchViewModel : ViewModel() {

    private val _bestMatches = MutableLiveData<List<MatchResult>>()
    val bestMatches: LiveData<List<MatchResult>> = _bestMatches

    fun loadMatches() {
        // TEMP mock data (replace with real matching logic)
        _bestMatches.value = listOf(
            MatchResult("Alex", 85),
            MatchResult("Jordan", 78),
            MatchResult("Taylor", 72)
        )
    }
}
