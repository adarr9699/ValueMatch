package com.example.valuematch.data.models

data class ChatMessage(
    val from: String = "",
    val to: String = "",
    val text: String = "",
    val createdAt: Any? = null
)