package com.example.valuematch.repository

import com.google.firebase.firestore.FirebaseFirestore

class LikeRepository {

    private val db = FirebaseFirestore.getInstance()

    fun like(from: String, to: String) {
        db.collection("likes").add(
            mapOf("fromUser" to from, "toUser" to to)
        )
    }
}
