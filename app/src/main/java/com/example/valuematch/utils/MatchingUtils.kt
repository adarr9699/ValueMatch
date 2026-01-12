package com.example.valuematch.utils

import com.example.valuematch.models.MatchResult
import com.example.valuematch.models.UserProfile

object MatchingUtils {

    fun calculateMatch(
        userA: UserProfile,
        userB: UserProfile
    ): MatchResult {

        val sharedValues =
            userA.values.intersect(userB.values.toSet()).toList()

        val matchScore =
            if (userA.values.isNotEmpty()) {
                (sharedValues.size * 100) / userA.values.size
            } else {
                0
            }

        return MatchResult(
            userId = userB.uid,
            matchScore = matchScore,
            sharedValues = sharedValues
        )
    }
}
