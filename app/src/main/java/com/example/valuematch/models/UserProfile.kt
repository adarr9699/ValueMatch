package com.example.valuematch.models

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val about: String = "",
    val values: List<String> = emptyList()
)
