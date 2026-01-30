package com.example.valuematch.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

data class MatchItem(
    val matchId: String = "",
    val userA: String = "",
    val userB: String = "",
    val participants: List<String> = emptyList(),
    val createdAt: Long = 0L
) {
    fun otherUserId(myUid: String): String {
        return if (userA == myUid) userB else userA
    }
}

class MatchRepository {

    private val db = FirebaseFirestore.getInstance()
    private val matchesCol = db.collection("matches")

    private fun matchId(a: String, b: String): String =
        if (a < b) "${a}_${b}" else "${b}_${a}"

    fun createMatch(a: String, b: String): Task<Void> {
        val id = matchId(a, b)
        val userA = if (a < b) a else b
        val userB = if (a < b) b else a

        val data = mapOf(
            "matchId" to id,
            "userA" to userA,
            "userB" to userB,
            "participants" to listOf(userA, userB),
            "createdAt" to System.currentTimeMillis()
        )

        return matchesCol.document(id).set(data, SetOptions.merge())
    }

    fun listenMatchesForCurrentUser(
        myUid: String,
        onUpdate: (List<MatchItem>) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration {
        return matchesCol
            .whereArrayContains("participants", myUid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    onError(err)
                    return@addSnapshotListener
                }

                val list = snap?.documents?.mapNotNull { doc ->
                    val m = doc.toObject(MatchItem::class.java)
                    m?.copy(matchId = doc.id)
                } ?: emptyList()

                onUpdate(list)
            }
    }
}
