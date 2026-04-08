package com.example.valuematch.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MatchRepository {

    private val db = FirebaseFirestore.getInstance()

    private val uid: String
        get() = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Not logged in")

    fun listenOtherUserIdsForMatches(
        onUpdate: (List<String>) -> Unit,
        onError: (Exception) -> Unit,
        myUid: String
    ): ListenerRegistration {

        return db.collection("matches")
            .whereArrayContains("users", uid)
            .addSnapshotListener { snap, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snap == null) {
                    onUpdate(emptyList())
                    return@addSnapshotListener
                }

                val otherIds = snap.documents.mapNotNull { doc ->
                    val users = doc.get("users") as? List<*>
                    users?.filterIsInstance<String>()?.firstOrNull { it != uid }
                }

                onUpdate(otherIds)
            }
    }
}