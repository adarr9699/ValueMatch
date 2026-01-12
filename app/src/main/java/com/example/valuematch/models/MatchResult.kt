package com.example.valuematch.models

data class MatchResult(
    val userId: String,
    val matchScore: Int,
    val sharedValues: List<String>
)