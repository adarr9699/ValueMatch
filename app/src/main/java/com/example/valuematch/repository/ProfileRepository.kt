package com.example.valuematch.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun loadProfile(onResult: (Map<String, Any>?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("profiles")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                onResult(doc.data)
            }
    }
}
