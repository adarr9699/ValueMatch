package com.example.valuematch.respository

import com.example.valuematch.models.UserProfile
import com.example.valuematch.utils.MatchingUtils

class MatchRepository {

    fun getMatches(currentUser: UserProfile, others: List<UserProfile>) =
        others.filter { it.uid != currentUser.uid }
            .map { other -> MatchingUtils.calculateMatchScore(currentUser, other) }
            .sortedByDescending { it.matchScore }
}