package com.example.valuematch.utils

object MatchingUtils {
    fun matchId(a: String, b: String): String =
        listOf(a, b).sorted().joinToString("_")
}