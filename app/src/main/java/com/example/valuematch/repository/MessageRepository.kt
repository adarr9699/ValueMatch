package com.example.valuematch.repository

import com.example.valuematch.data.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MessageRepository {

    private val db = FirebaseFirestore.getInstance()

    private val uid: String
        get() = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Not logged in")

    private fun chatId(a: String, b: String): String =
        listOf(a, b).sorted().joinToString("_")

    fun listenMessages(
        otherUserId: String,
        onUpdate: (List<ChatMessage>) -> Unit
    ): ListenerRegistration {

        val id = chatId(uid, otherUserId)

        return db.collection("chats")
            .document(id)
            .collection("messages")
            .orderBy("createdAt")
            .addSnapshotListener { snap, _ ->
                if (snap == null) {
                    onUpdate(emptyList())
                    return@addSnapshotListener
                }

                val messages = snap.documents.mapNotNull { doc ->
                    doc.toObject(ChatMessage::class.java)
                }

                onUpdate(messages)
            }
    }

    fun sendMessage(otherUserId: String, text: String) {
        val id = chatId(uid, otherUserId)

        db.collection("chats")
            .document(id)
            .collection("messages")
            .add(
                mapOf(
                    "from" to uid,
                    "to" to otherUserId,
                    "text" to text,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
    }

    fun blockUser(otherUserId: String) {
        val me = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("blocks")
            .document("${me}_$otherUserId")
            .set(
                mapOf(
                    "from" to me,
                    "to" to otherUserId,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
    }

    fun reportUser(otherUserId: String, reason: String) {
        val me = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("reports")
            .add(
                mapOf(
                    "from" to me,
                    "to" to otherUserId,
                    "reason" to reason,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
    }
}