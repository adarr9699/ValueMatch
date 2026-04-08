package com.example.valuematch.data.models

data class Message(
    val from: String = "",
    val to: String = "",
    val text: String = "",
    val createdAt: Long = 0L
)