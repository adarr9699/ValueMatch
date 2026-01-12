package com.example.valuematch.repository

import com.example.valuematch.models.Message
import com.google.firebase.firestore.FirebaseFirestore

class MessageRepository {

    private val db = FirebaseFirestore.getInstance()

    fun sendMessage(message: Message) {
        db.collection("messages").add(message)
    }

    fun getMessages(userA: String, userB: String, callback: (List<Message>) -> Unit) {
        db.collection("messages")
            .whereIn("from", listOf(userA, userB))
            .get()
            .addOnSuccessListener {
                callback(it.toObjects(Message::class.java))
            }
    }
}