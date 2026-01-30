package com.example.valuematch.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class LikeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val likesCol = db.collection("likes")

    private fun likeId(from: String, to: String) = "${from}_$to"

    fun saveLike(from: String, to: String): Task<Void> {
        val data = mapOf(
            "fromUser" to from,
            "toUser" to to,
            "createdAt" to System.currentTimeMillis()
        )
        return likesCol.document(likeId(from, to)).set(data, SetOptions.merge())
    }

    fun didLike(from: String, to: String) =
        likesCol.document(likeId(from, to)).get()
}
