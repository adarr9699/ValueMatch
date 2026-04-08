package com.example.valuematch.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {

    private val db = FirebaseFirestore.getInstance()

    fun getUser(uid: String): Task<DocumentSnapshot> {
        return db.collection("users").document(uid).get()
    }
}