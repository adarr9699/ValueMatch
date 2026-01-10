package com.example.valuematch.models

import com.google.protobuf.Value

data class UserProfile(
    val userId: String = "",
    val name: String = "",
    val value: List<String> = emptyList(),
)
