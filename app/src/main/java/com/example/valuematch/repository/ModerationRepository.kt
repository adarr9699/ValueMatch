package com.example.valuematch.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ModerationRepository {

    private val db = FirebaseFirestore.getInstance()

    private val uid: String
        get() = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Not logged in")

    private fun docId(me: String, other: String) = "${me}_$other"

    fun blockUser(otherUserId: String) {
        val me = uid
        if (otherUserId.isBlank() || otherUserId == me) return

        db.collection("blocks")
            .document(docId(me, otherUserId))
            .set(
                mapOf(
                    "from" to me,
                    "to" to otherUserId,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
    }

    fun reportUser(otherUserId: String, reason: String) {
        val me = uid
        if (otherUserId.isBlank() || otherUserId == me) return

        db.collection("reports")
            .add(
                mapOf(
                    "from" to me,
                    "to" to otherUserId,
                    "reason" to reason,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
    }
}