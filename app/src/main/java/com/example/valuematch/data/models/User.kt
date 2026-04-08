package com.example.valuematch.data.models

data class User(
    val userId: String = "",
    val name: String = "",
    val age: Int = 0,
    val about: String = "",
    val photoUrl: String = "",
    val values: List<String> = emptyList()
)