package com.example.valuematch.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun uidOrThrow(): String = auth.currentUser?.uid
        ?: throw IllegalStateException("User not logged in")
}
