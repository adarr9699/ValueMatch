package com.example.valuematch.models

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val values: List<String> = emptyList(),
    val age: Int = 0,
    val bio: String = ""
)
