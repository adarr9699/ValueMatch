package com.example.valuematch.utils

object MatchingUtils {
    fun sharedValues(a: List<String>, b: List<String>): List<String> {
        if (a.isEmpty() || b.isEmpty()) return emptyList()
        val setB = b.toSet()
        return a.filter { it in setB }.distinct()
    }

    fun chatId(uidA: String, uidB: String): String {
        return if (uidA < uidB) "${uidA}_$uidB" else "${uidB}_$uidA"
    }
}
