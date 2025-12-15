package com.example.valuematch.respository

import com.example.valuematch.models.UserProfile
import com.example.valuematch.utils.FirebaseUtils
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val db = FirebaseUtils.db

    suspend fun saveProfile(profile: UserProfile) {
        db.collection("profiles")
            .document(profile.uid)
            .set(profile)
            .await()
    }

    suspend fun getAllProfiles(): List<UserProfile> {
        val snapshot = db.collection("profiles").get().await()
        return snapshot.toObjects(UserProfile::class.java)
    }
}