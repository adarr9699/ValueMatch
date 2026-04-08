package com.example.valuematch.repository

import com.example.valuematch.data.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val col = db.collection("users")

    fun getUser(uid: String): Task<com.google.firebase.firestore.DocumentSnapshot> {
        return col.document(uid).get()
    }

    fun upsertUser(user: User): Task<Void> {
        return col.document(user.userId).set(user, SetOptions.merge())
    }
}