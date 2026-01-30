package com.example.valuematch.data.models

data class MatchResult(
    val otherUserId: String = "",
    val otherName: String = "",
    val otherBio: String = "",
    val sharedValues: List<String> = emptyList()
)
