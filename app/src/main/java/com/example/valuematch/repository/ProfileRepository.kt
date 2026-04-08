package com.example.valuematch.repository

import com.example.valuematch.data.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getProfile(uid: String): UserProfile? {
        val snap = db.collection("users").document(uid).get().await()
        return snap.toObject(UserProfile::class.java)
    }

    suspend fun upsertProfile(profile: UserProfile) {
        db.collection("users").document(profile.uid)
            .set(profile)
            .await()
    }

    suspend fun updateValues(uid: String, values: List<String>) {
        db.collection("users").document(uid)
            .update(
                mapOf(
                    "values" to values,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
    }
}