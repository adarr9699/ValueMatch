package com.example.valuematch.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel(){
    private val db = FirebaseFirestore.getInstance()

    fun saveProfile(userName: String, selectedValues: List<String>) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val userMap = hashMapOf(
            "name" to userName,
            "values" to selectedValues
        )

        db.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener { Log.d("Firestore", "Profile saved!") }
            .addOnFailureListener { e -> Log.e("Firestore", "Error saving profile", e) }
    }
}





