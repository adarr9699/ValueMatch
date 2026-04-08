package com.example.valuematch.utils

object ChatIdUtils {
    fun matchId(a: String, b: String): String =
        listOf(a, b).sorted().joinToString("_")
}
