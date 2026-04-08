package com.example.valuematch.utils

import com.google.firebase.auth.FirebaseAuth

fun uidOrThrow(): String {
    return FirebaseAuth.getInstance().currentUser?.uid
        ?: throw IllegalStateException("User not logged in")
}