package com.example.valuematch.utils

import com.example.valuematch.models.MatchResult
import com.example.valuematch.models.UserProfile

object MatchingUtils {

    fun calculateMatchScore(userA: UserProfile, userB: UserProfile): MatchResult {
        val shared = userA.values.intersect(userB.values.toSet()).toList()
        val score = (shared.size * 100) / maxOf(userA.values.size, 1)

        return MatchResult(
            userId = userB.uid,
            matchScore = score,
            sharedValues = shared
        )
    }
}
