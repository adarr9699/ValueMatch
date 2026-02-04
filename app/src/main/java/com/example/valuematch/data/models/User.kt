package com.example.valuematch.data.models

data class User(
    val userId: String = "",
    val name: String = "",
    val bio: String = "",
    val values: List<String> = emptyList(),
    val gender: String = "",
    val location: String = ""
)

