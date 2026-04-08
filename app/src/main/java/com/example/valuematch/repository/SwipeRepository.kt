package com.example.valuematch.repository

import com.example.valuematch.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SwipeRepository {

    private val db = FirebaseFirestore.getInstance()

    private val uid: String
        get() = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Not logged in")

    private fun likeDocId(from: String, to: String) = "${from}_$to"
    private fun matchId(a: String, b: String): String =
        listOf(a, b).sorted().joinToString("_")

    suspend fun loadSwipeCandidates(limit: Long = 30): List<User> {
        val me = uid

        val usersSnap = db.collection("users")
            .limit(limit)
            .get()
            .await()

        val all = usersSnap.toObjects(User::class.java)
            .filter { it.userId.isNotBlank() && it.userId != me }

        val likedSnap = db.collection("likes")
            .whereEqualTo("from", me)
            .get()
            .await()

        val passedSnap = db.collection("passes")
            .whereEqualTo("from", me)
            .get()
            .await()

        val seen = buildSet {
            likedSnap.documents.forEach { add(it.getString("to").orEmpty()) }
            passedSnap.documents.forEach { add(it.getString("to").orEmpty()) }
        }.filter { it.isNotBlank() }.toSet()

        return all.filter { it.userId !in seen }
    }

    suspend fun passUser(otherUserId: String) {
        val me = uid
        if (otherUserId.isBlank() || otherUserId == me) return

        val docId = likeDocId(me, otherUserId)
        db.collection("passes")
            .document(docId)
            .set(
                mapOf(
                    "from" to me,
                    "to" to otherUserId,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
            .await()
    }

    suspend fun likeUser(otherUserId: String): Boolean {
        val me = uid
        if (otherUserId.isBlank() || otherUserId == me) return false

        val myLikeId = likeDocId(me, otherUserId)
        db.collection("likes")
            .document(myLikeId)
            .set(
                mapOf(
                    "from" to me,
                    "to" to otherUserId,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
            .await()

        val theirLikeId = likeDocId(otherUserId, me)
        val theirLike = db.collection("likes").document(theirLikeId).get().await()

        if (theirLike.exists()) {
            val mId = matchId(me, otherUserId)
            db.collection("matches")
                .document(mId)
                .set(
                    mapOf(
                        "users" to listOf(me, otherUserId),
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                )
                .await()
            return true
        }

        return false
    }
}