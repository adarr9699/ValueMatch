package com.example.valuematch.repository

import com.example.valuematch.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class MatchRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getAllUsers(callback: (List<UserProfile>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.toObjects(UserProfile::class.java))
            }
    }
}
