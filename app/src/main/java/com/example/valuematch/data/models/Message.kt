package com.example.valuematch.data.models

data class Message(
    val id: String = "",
    val fromUserId: String = "",
    val toUserId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
