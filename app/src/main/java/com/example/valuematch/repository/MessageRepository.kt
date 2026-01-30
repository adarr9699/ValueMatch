package com.example.valuematch.repository

import com.example.valuematch.data.models.Message
import com.example.valuematch.utils.FirebaseUtils
import com.example.valuematch.utils.MatchingUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query

class MessageRepository {

    fun listenMessages(otherUid: String, onUpdate: (List<Message>) -> Unit) {
        val me = FirebaseUtils.uidOrThrow()
        val chatId = MatchingUtils.chatId(me, otherUid)

        FirebaseUtils.db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull { it.toObject(Message::class.java) } ?: emptyList()
                onUpdate(list)
            }
    }

    fun sendMessage(toUserId: String, text: String): Task<Void> {
        val me = FirebaseUtils.uidOrThrow()
        val chatId = MatchingUtils.chatId(me, toUserId)
        val doc = FirebaseUtils.db.collection("chats")
            .document(chatId)
            .collection("messages")
            .document()

        val msg = Message(
            id = doc.id,
            fromUserId = me,
            toUserId = toUserId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        return doc.set(msg)
    }
}
