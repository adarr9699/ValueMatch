package com.example.valuematch.data.models

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val age: Int = 0,
    val about: String = "",
    val photoUrl: String = "",
    val values: List<String> = emptyList(),
    val updatedAt: Long = 0L
)