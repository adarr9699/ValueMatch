package com.example.valuematch.data.models

data class Match(
    val userId: String = "",
    val sharedValues: List<String> = emptyList()
)
