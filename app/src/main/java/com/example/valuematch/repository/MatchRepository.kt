package com.example.valuematch.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MatchRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getMatches(onResult: (List<String>) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("matches")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val matches = doc.get("matches") as? List<String> ?: emptyList()
                onResult(matches)
            }
    }
}
