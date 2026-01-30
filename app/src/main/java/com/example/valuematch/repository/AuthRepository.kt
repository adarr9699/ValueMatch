package com.example.valuematch.repository

import com.example.valuematch.utils.FirebaseUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class AuthRepository {
    fun signup(email: String, password: String): Task<AuthResult> {
        return FirebaseUtils.auth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return FirebaseUtils.auth.signInWithEmailAndPassword(email, password)
    }

    fun logout() {
        FirebaseUtils.auth.signOut()
    }
}
