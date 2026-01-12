package com.example.valuematch.repository

import com.example.valuematch.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveProfile(profile: UserProfile, callback: (Boolean) -> Unit) {
        db.collection("users")
            .document(profile.uid)
            .set(profile)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun getAllUsers(callback: (List<UserProfile>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.toObjects(UserProfile::class.java))
            }
    }
}

