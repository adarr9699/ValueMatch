package com.example.valuematch.repository

import com.example.valuematch.data.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCol = db.collection("users")

    fun getUser(uid: String): Task<DocumentSnapshot> {
        return usersCol.document(uid).get()
    }

    fun upsertUser(user: User): Task<Void> {
        val uid = user.userId
        require(uid.isNotBlank()) { "User.userId is blank. Can't save user." }

        return usersCol.document(uid)
            .set(user, SetOptions.merge())
    }

    fun listenAllUsers(
        onUpdate: (List<User>) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration {
        return usersCol
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    onError(err)
                    return@addSnapshotListener
                }

                val list = snap?.documents?.mapNotNull { doc ->
                    val user = doc.toObject(User::class.java)
                    user?.copy(userId = doc.id)
                } ?: emptyList()

                onUpdate(list)
            }
    }
}
